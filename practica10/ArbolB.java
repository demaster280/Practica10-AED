/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practica10;

import java.util.ArrayList;
import java.util.List;


public class ArbolB {
    
    private List<Integer> caminoRecorrido;
    
    NodoArbolB root;
    int t;

    //Constructor
    public ArbolB(int t) {
        this.t = t;
        root = new NodoArbolB(t); 
        caminoRecorrido = new ArrayList<>();
    }

    
    //Busca el valor ingresado y muestra el contenido del nodo que contiene el valor
    public void buscarNodoPorClave(int num) {
        NodoArbolB temp = search(root, num);

        if (temp == null) {
            System.out.println("No se ha encontrado un nodo con el valor ingresado");
        } else {
            caminoRecorrido.clear();
            print(temp);
            mostrarCaminoRecorrido();
        }
    }

    //Search
    private NodoArbolB search(NodoArbolB actual, int key) {
        int i = 0;//se empieza a buscar siempre en la primera posicion

        //Incrementa el indice mientras el valor de la clave del nodo sea menor
        while (i < actual.n && key > actual.key[i]) {
            i++;
        }

        //Si la clave es igual, entonces retornamos el nodo
        if (i < actual.n && key == actual.key[i]) {
            return actual;
        }

        //Si llegamos hasta aqui, entonces hay que buscar los hijos
        //Se revisa primero si tiene hijos
        if (actual.leaf) {
            return null;
        } else {
            //Si tiene hijos, hace una llamada recursiva
            return search(actual.child[i], key);
        }
    }

    public void insertar(int key) {
        NodoArbolB r = root;

        //Si el nodo esta lleno lo debe separar antes de insertar
        if (r.n == ((2 * t) - 1)) {
            NodoArbolB s = new NodoArbolB(t);
            root = s;
            s.leaf = false;
            s.n = 0;
            s.child[0] = r;
            split(s, 0, r);
            nonFullInsert(s, key);
        } else {
            nonFullInsert(r, key);
        }
    }
    private void split(NodoArbolB x, int i, NodoArbolB y) {
        //Nodo temporal que sera el hijo i + 1 de x
        NodoArbolB z = new NodoArbolB(t);
        z.leaf = y.leaf;
        z.n = (t - 1);

        //Copia las ultimas (t - 1) claves del nodo y al inicio del nodo z      // z = |40|50| | | |
        for (int j = 0; j < (t - 1); j++) {
            z.key[j] = y.key[(j + t)];
        }

        //Si no es hoja hay que reasignar los nodos hijos
        if (!y.leaf) {
            for (int k = 0; k < t; k++) {
                z.child[k] = y.child[(k + t)];
            }
        }

        //nuevo tamanio de y                                                    // x =            | | | | | |
        y.n = (t - 1);                                                          //               /   \
                                                                                //  |10|20| | | |
        //Mueve los hijos de x para darle espacio a z
        for (int j = x.n; j > i; j--) {
            x.child[(j + 1)] = x.child[j];
        }
        //Reasigna el hijo (i+1) de x                                           // x =            | | | | | |
        x.child[(i + 1)] = z;                                                   //               /   \
                                                                                //  |10|20| | | |     |40|50| | | |
        //Mueve las claves de x
        for (int j = x.n; j > i; j--) {
            x.key[(j + 1)] = x.key[j];
        }

        //Agrega la clave situada en la mediana                                 // x =            |30| | | | |
        x.key[i] = y.key[(t - 1)];                                              //               /    \
        x.n++;                                                                  //  |10|20| | | |      |40|50| | | |
    } 
    
    private void nonFullInsert(NodoArbolB x, int key) {
        //Si es una hoja
        if (x.leaf) {
            int i = x.n; //cantidad de valores del nodo
            //busca la posicion i donde asignar el valor
            while (i >= 1 && key < x.key[i - 1]) {
                x.key[i] = x.key[i - 1];//Desplaza los valores mayores a key
                i--;
            }

            x.key[i] = key;//asigna el valor al nodo
            x.n++; //aumenta la cantidad de elementos del nodo
            
            caminoRecorrido.add(key);
            
        } else {
            int j = 0;
            //Busca la posicion del hijo
            while (j < x.n && key > x.key[j]) {
                j++;
            }

            //Si el nodo hijo esta lleno lo separa
            if (x.child[j].n == (2 * t - 1)) {
                split(x, j, x.child[j]);

                if (key > x.key[j]) {
                    j++;
                }
            }

            nonFullInsert(x.child[j], key);
        }
    }
    
    // Elimina una clave del árbol
    public void eliminarClave(int key) {
        eliminar(root, key);

        // Si la raíz se quedó sin claves y tiene un hijo, la raíz se reemplaza por su único hijo
        if (root.n == 0 && !root.leaf) {
            root = root.child[0];
        }
    }
    
    // Método para buscar el índice de una clave en un nodo
    private int buscarClaveIndex(NodoArbolB nodo, int clave) {
        int index = 0;
        while (index < nodo.n && clave > nodo.key[index]) {
            index++;
        }
        return index;
    }

    private void eliminar(NodoArbolB x, int key) {
        int i = buscarClaveIndex(x, key);

        // Si la clave se encuentra en el nodo x
        if (i < x.n && x.key[i] == key) {
            // Caso 1: La clave se encuentra en el nodo x y es una hoja
            if (x.leaf) {
                eliminarDeHoja(x, i);
            }
            // Caso 2: La clave se encuentra en el nodo x y no es una hoja
            else {
                eliminarDeNoHoja(x, i);
            }
        }
        // La clave no se encuentra en el nodo x
        else {
            // Caso 3: La clave se encuentra en un subárbol enraizado por el hijo i
            if (!x.leaf) {
                eliminarDeSubarbol(x, i, key);
            }
        }
    }

    private void eliminarDeHoja(NodoArbolB x, int index) {
        // Mueve todas las claves después del índice hacia atrás
        for (int i = index + 1; i < x.n; i++) {
            x.key[i - 1] = x.key[i];
        }
        caminoRecorrido.remove(caminoRecorrido.size() - 1); // elimina la clave del camino recorrido

        x.n--;
    }
    private int obtenerPredecesor(NodoArbolB nodo, int indice) {
        NodoArbolB subArbol = nodo.child[indice];
        while (!subArbol.leaf) {
            subArbol = subArbol.child[subArbol.n];
        }
        return subArbol.key[subArbol.n - 1];
    } 
    
     // Método para obtener el sucesor de una clave en un subárbol
    private int obtenerSucesor(NodoArbolB nodo, int indice) {
        NodoArbolB subArbol = nodo.child[indice + 1];
        while (!subArbol.leaf) {
            subArbol = subArbol.child[0];
        }
        return subArbol.key[0];
    }
    
    private void eliminarDeNoHoja(NodoArbolB x, int index) {
        int key = x.key[index];

        // Caso 2a: El predecesor de key (máximo valor en el subárbol izquierdo) tiene al menos t claves
        if (x.child[index].n >= t) {
            int predecessor = obtenerPredecesor(x, index);
            x.key[index] = predecessor;
            eliminar(x.child[index], predecessor);
        }
        // Caso 2b: El sucesor de key (mínimo valor en el subárbol derecho) tiene al menos t claves
        else if (x.child[index + 1].n >= t) {
            int successor = obtenerSucesor(x, index);
            x.key[index] = successor;
            eliminar(x.child[index + 1], successor);
        }
        // Caso 2c: Ambos hijos de key tienen t-1 claves, se fusionan en un solo hijo
        else {
            NodoArbolB child = x.child[index];
            NodoArbolB sibling = x.child[index + 1];

            // Mueve la clave de x al hijo child
            child.key[t - 1] = key;

            // Copia las claves y los hijos del hermano a child
            for (int i = 0; i < sibling.n; i++) {
                child.key[i + t] = sibling.key[i];
            }

            if (!child.leaf) {
                for (int i = 0; i <= sibling.n; i++) {
                    child.child[i + t] = sibling.child[i];
                }
            }

            // Desplaza las claves en x hacia atrás para llenar el espacio dejado por key
            for (int i = index + 1; i < x.n; i++) {
                x.key[i - 1] = x.key[i];
            }

            // Desplaza los punteros de los hijos en x hacia atrás
            for (int i = index + 2; i <= x.n; i++) {
                x.child[i - 1] = x.child[i];
            }

            x.n--;

            // Elimina la clave de key en child
            eliminar(child, key);
        }
    }

    private void eliminarDeSubarbol(NodoArbolB x, int index, int key) {
        NodoArbolB child = x.child[index];
        NodoArbolB sibling = null;

        // Si el hijo tiene solo t-1 claves, lo rellenamos
        if (child.n == t - 1) {
            // Verifica si el hijo tiene un hermano izquierdo con al menos t claves
            if (index > 0 && x.child[index - 1].n >= t) {
                sibling = x.child[index - 1];
            }
            // Verifica si el hijo tiene un hermano derecho con al menos t claves
            else if (index < x.n && x.child[index + 1].n >= t) {
                sibling = x.child[index + 1];
            }

            // Si el hijo tiene un hermano con al menos t claves, realiza una rotación
            if (sibling != null) {
                // Caso 3a: El hijo tiene un hermano izquierdo con al menos t claves
                if (index > 0 && x.child[index - 1] == sibling) {
                    // Mueve la clave en x a child
                    child.key[0] = x.key[index - 1];

                    // Mueve la clave en el hermano al final de child
                    child.key[1] = sibling.key[sibling.n - 1];

                    // Mueve el último hijo del hermano a child si no es hoja
                    if (!child.leaf) {
                        child.child[1] = sibling.child[sibling.n];
                    }

                    // Desplaza las claves en x hacia atrás para llenar el espacio dejado por la clave
                    for (int i = index; i < x.n - 1; i++) {
                        x.key[i - 1] = x.key[i];
                    }

                    // Desplaza los punteros de los hijos en x hacia atrás
                    for (int i = index; i < x.n; i++) {
                        x.child[i] = x.child[i + 1];
                    }

                    // Actualiza las cantidades de claves en child y el hermano
                    child.n++;
                    sibling.n--;

                    eliminar(child, key);
                }
                // Caso 3a: El hijo tiene un hermano derecho con al menos t claves
                else {
                    // Mueve la clave en x a child
                    child.key[t - 1] = x.key[index];

                    // Mueve la clave en el hermano al principio de child
                    child.key[t - 2] = sibling.key[0];

                    // Mueve el primer hijo del hermano a child si no es hoja
                    if (!child.leaf) {
                        child.child[t - 1] = sibling.child[0];
                    }

                    // Desplaza las claves en el hermano hacia atrás para llenar el espacio dejado por la clave
                    for (int i = 1; i < sibling.n; i++) {
                        sibling.key[i - 1] = sibling.key[i];
                    }

                    // Desplaza los punteros de los hijos en el hermano hacia atrás
                    for (int i = 1; i <= sibling.n; i++) {
                        sibling.child[i - 1] = sibling.child[i];
                    }

                    // Actualiza las cantidades de claves en child y el hermano
                    child.n++;
                    sibling.n--;

                    eliminar(child, key);
                }
            }
            // Si el hijo no tiene hermanos con al menos t claves, se fusiona con un hermano
            else {
                // Caso 3c: Fusiona el hijo con su hermano derecho
                if (index < x.n) {
                    sibling = x.child[index + 1];

                    // Mueve la clave en x al hijo
                    child.key[t - 1] = x.key[index];

                    // Copia las claves y los hijos del hermano al final del hijo
                    for (int i = 0; i < sibling.n; i++) {
                        child.key[i + t] = sibling.key[i];
                    }

                    if (!child.leaf) {
                        for (int i = 0; i <= sibling.n; i++) {
                            child.child[i + t] = sibling.child[i];
                        }
                    }

                    // Desplaza las claves en x hacia atrás para llenar el espacio dejado por la clave
                    for (int i = index + 1; i < x.n; i++) {
                        x.key[i - 1] = x.key[i];
                    }

                    // Desplaza los punteros de los hijos en x hacia atrás
                    for (int i = index + 2; i <= x.n; i++) {
                        x.child[i - 1] = x.child[i];
                    }

                    // Actualiza las cantidades de claves en child y el hermano
                    child.n += sibling.n + 1;
                    x.n--;

                    eliminar(child, key);
                }
                // Caso 3c: Fusiona el hijo con su hermano izquierdo
                else {
                    sibling = x.child[index - 1];

                    // Mueve la clave en x al hermano
                    sibling.key[t - 1] = x.key[index - 1];

                    // Copia las claves y los hijos del hijo al final del hermano
                    for (int i = 0; i < child.n; i++) {
                        sibling.key[i + t] = child.key[i];
                    }

                    if (!sibling.leaf) {
                        for (int i = 0; i <= child.n; i++) {
                            sibling.child[i + t] = child.child[i];
                        }
                    }

                    // Desplaza las claves en x hacia atrás para llenar el espacio dejado por la clave
                    for (int i = index; i < x.n - 1; i++) {
                        x.key[i - 1] = x.key[i];
                    }

                    // Desplaza los punteros de los hijos en x hacia atrás
                    for (int i = index - 1; i < x.n; i++) {
                        x.child[i] = x.child[i + 1];
                    }

                    // Actualiza las cantidades de claves en el hermano y x
                    sibling.n += child.n + 1;
                    x.n--;

                    eliminar(sibling, key);
                }
            }
        }
        // Si el hijo tiene al menos t claves, elimina la clave del hijo recursivamente
        else {
            eliminar(child, key);
        }
    }
    
   
    private void mostrarCaminoRecorrido() {
        System.out.print("Camino recorrido: ");
        for (Integer clave : caminoRecorrido) {
            System.out.print(clave + " ");
        }
        System.out.println();
    }
        
    public void showBTree() {
        print(root);
    }

    //Print en preorder
    private void print(NodoArbolB n) {
        n.imprimir();

        //Si no es hoja
        if (!n.leaf) {
            //recorre los nodos para saber si tiene hijos
            for (int j = 0; j <= n.n; j++) {
                if (n.child[j] != null) {
                    System.out.println();
                    print(n.child[j]);
                }
            }
        }
    }

    
}
