




package B_Tree;

import java.util.ArrayList;
import java.util.List;

public class ArbolB {
    private NodoArbolB raiz;
    private int gradoMinimo;
    
    public ArbolB(int gradoMinimo) {
        this.raiz = null;
        this.gradoMinimo = gradoMinimo;
    }
    
    public void insertar(int valor) {
        if (raiz == null) {
            raiz = new NodoArbolB(gradoMinimo);
            raiz.key[0] = valor;
            raiz.n = 1;
        } else {
            if (raiz.n == ((2 * gradoMinimo) - 1)) {
                NodoArbolB nuevoNodo = new NodoArbolB(gradoMinimo);
                nuevoNodo.child[0] = raiz;
                raiz = nuevoNodo;
                dividirNodo(nuevoNodo, 0, raiz);
                insertarEnNodo(raiz, valor);
            } else {
                insertarEnNodo(raiz, valor);
            }
        }
    }
    
    private void insertarEnNodo(NodoArbolB nodo, int valor) {
        int i = nodo.n - 1;
        if (nodo.leaf) {
            while (i >= 0 && valor < nodo.key[i]) {
                nodo.key[i + 1] = nodo.key[i];
                i--;
            }
            nodo.key[i + 1] = valor;
            nodo.n++;
        } else {
            while (i >= 0 && valor < nodo.key[i]) {
                i--;
            }
            i++;
            if (nodo.child[i].n == ((2 * gradoMinimo) - 1)) {
                dividirNodo(nodo, i, nodo.child[i]);
                if (valor > nodo.key[i]) {
                    i++;
                }
            }
            insertarEnNodo(nodo.child[i], valor);
        }
    }
    
    private void dividirNodo(NodoArbolB padre, int indiceHijo, NodoArbolB nodoCompleto) {
        NodoArbolB nuevoNodo = new NodoArbolB(gradoMinimo);
        nuevoNodo.leaf = nodoCompleto.leaf;
        nuevoNodo.n = gradoMinimo - 1;
        for (int j = 0; j < gradoMinimo - 1; j++) {
            nuevoNodo.key[j] = nodoCompleto.key[j + gradoMinimo];
        }
        if (!nodoCompleto.leaf) {
            for (int j = 0; j < gradoMinimo; j++) {
                nuevoNodo.child[j] = nodoCompleto.child[j + gradoMinimo];
            }
        }
        nodoCompleto.n = gradoMinimo - 1;
        for (int j = padre.n; j > indiceHijo; j--) {
            padre.child[j + 1] = padre.child[j];
        }
        padre.child[indiceHijo + 1] = nuevoNodo;
        for (int j = padre.n - 1; j >= indiceHijo; j--) {
            padre.key[j + 1] = padre.key[j];
        }
        padre.key[indiceHijo] = nodoCompleto.key[gradoMinimo - 1];
        padre.n++;
    }
    
    public void eliminar(int valor) {
        if (raiz == null) {
            return;
        }
        eliminarEnNodo(raiz, valor);
        if (raiz.n == 0) {
            if (raiz.leaf) {
                raiz = null;
            } else {
                raiz = raiz.child[0];
            }
        }
    }
    
    private void eliminarEnNodo(NodoArbolB nodo, int valor) {
        int indiceClave = nodo.find(valor);
        if (indiceClave != -1) {
            if (nodo.leaf) {
                eliminarDeHoja(nodo, indiceClave);
            } else {
                eliminarDeNoHoja(nodo, indiceClave);
            }
        } else {
            if (nodo.leaf) {
                return;
            }
            int indiceHijo = obtenerIndiceHijo(nodo, valor);
            if (nodo.child[indiceHijo].n < gradoMinimo) {
                llenarNodo(nodo, indiceHijo);
            }
            eliminarEnNodo(nodo.child[indiceHijo], valor);
        }
    }
    
    private void eliminarDeHoja(NodoArbolB nodo, int indiceClave) {
        for (int i = indiceClave + 1; i < nodo.n; i++) {
            nodo.key[i - 1] = nodo.key[i];
        }
        nodo.n--;
    }
    
    private void eliminarDeNoHoja(NodoArbolB nodo, int indiceClave) {
        int clave = nodo.key[indiceClave];
        if (nodo.child[indiceClave].n >= gradoMinimo) {
            int clavePredecesora = obtenerClavePredecesora(nodo, indiceClave);
            nodo.key[indiceClave] = clavePredecesora;
            eliminarEnNodo(nodo.child[indiceClave], clavePredecesora);
        } else if (nodo.child[indiceClave + 1].n >= gradoMinimo) {
            int claveSucesora = obtenerClaveSucesora(nodo, indiceClave);
            nodo.key[indiceClave] = claveSucesora;
            eliminarEnNodo(nodo.child[indiceClave + 1], claveSucesora);
        } else {
            fusionarNodos(nodo, indiceClave);
            eliminarEnNodo(nodo.child[indiceClave], clave);
        }
    }
    
    private int obtenerIndiceHijo(NodoArbolB nodo, int valor) {
        int indiceHijo = 0;
        while (indiceHijo < nodo.n && valor > nodo.key[indiceHijo]) {
            indiceHijo++;
        }
        return indiceHijo;
    }
    
    private int obtenerClavePredecesora(NodoArbolB nodo, int indiceClave) {
        NodoArbolB nodoActual = nodo.child[indiceClave];
        while (!nodoActual.leaf) {
            nodoActual = nodoActual.child[nodoActual.n];
        }
        return nodoActual.key[nodoActual.n - 1];
    }
    
    private int obtenerClaveSucesora(NodoArbolB nodo, int indiceClave) {
        NodoArbolB nodoActual = nodo.child[indiceClave + 1];
        while (!nodoActual.leaf) {
            nodoActual = nodoActual.child[0];
        }
        return nodoActual.key[0];
    }
    
    private void fusionarNodos(NodoArbolB nodo, int indiceClave) {
        NodoArbolB hijo = nodo.child[indiceClave];
        NodoArbolB hermano = nodo.child[indiceClave + 1];
        hijo.key[gradoMinimo - 1] = nodo.key[indiceClave];
        for (int i = 0; i < hermano.n; i++) {
            hijo.key[i + gradoMinimo] = hermano.key[i];
        }
        if (!hijo.leaf) {
            for (int i = 0; i <= hermano.n; i++) {
                hijo.child[i + gradoMinimo] = hermano.child[i];
            }
        }
        for (int i = indiceClave + 1; i < nodo.n; i++) {
            nodo.key[i - 1] = nodo.key[i];
        }
        for (int i = indiceClave + 2; i <= nodo.n; i++) {
            nodo.child[i - 1] = nodo.child[i];
        }
        hijo.n += hermano.n + 1;
        nodo.n--;
    }
    
    public List<Integer> obtenerCaminoRecorrido(int valor) {
        List<Integer> camino = new ArrayList<>();
        if (raiz == null) {
            return camino;
        }
        obtenerCaminoRecorridoAux(raiz, valor, camino);
        return camino;
    }
    
    private boolean obtenerCaminoRecorridoAux(NodoArbolB nodo, int valor, List<Integer> camino) {
        int indiceClave = nodo.find(valor);
        if (indiceClave != -1) {
            camino.add(nodo.key[indiceClave]);
            return true;
        } else {
            int indiceHijo = obtenerIndiceHijo(nodo, valor);
            camino.add(nodo.key[indiceHijo]);
            return obtenerCaminoRecorridoAux(nodo.child[indiceHijo], valor, camino);
        }
    }
    
    public void showBTree() {
        if (raiz != null) {
            raiz.imprimir();
        }
    }
    
    public List<Integer> buscarNodo(int valor) {
        if (raiz == null) {
            return new ArrayList<>();
        }
        return buscarNodoAux(raiz, valor);
    }
    private List<Integer> buscarNodoAux(NodoArbolB nodo, int valor) {
        int indiceClave = nodo.find(valor);
        if (indiceClave != -1) {
            List<Integer> claves = new ArrayList<>();
            for (int i = 0; i < nodo.n; i++) {
                claves.add(nodo.key[i]);
            }
            return claves;
        } else {
            int indiceHijo = obtenerIndiceHijo(nodo, valor);
            return buscarNodoAux(nodo.child[indiceHijo], valor);
        }
    }
    public int obtenerValorMaximo() {
        if (raiz == null) {
            throw new IllegalStateException("El árbol está vacío");
        }
        NodoArbolB nodoActual = raiz;
        while (!nodoActual.leaf) {
            nodoActual = nodoActual.child[nodoActual.n];
        }
        return nodoActual.key[nodoActual.n - 1];
    }
    public NodoArbolB obtenerNodoMinimoRaiz() {
        if (raiz == null) {
            return null;
        }
        NodoArbolB nodoActual = raiz;
        // Desciende por los hijos izquierdos hasta llegar al nodo hoja más a la izquierda
        while (!nodoActual.leaf) {
            nodoActual = nodoActual.child[0];
        }
        return nodoActual;
    }    
}
