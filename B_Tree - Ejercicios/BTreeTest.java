


package B_Tree;
import java.util.List;
public class BTreeTest {
    public static void main(String[] args) {
        // Crear un árbol B con un grado mínimo de 3
        ArbolB arbolB = new ArbolB(3);
        // Insertar valores en el árbol
        arbolB.insertar(7);
        arbolB.insertar(3);
        arbolB.insertar(14);
        arbolB.insertar(1);
        arbolB.insertar(10);
        arbolB.insertar(5);
        arbolB.insertar(8);
        arbolB.insertar(12);
        arbolB.insertar(16);
        arbolB.insertar(2);
        arbolB.insertar(6);
        arbolB.insertar(11);
        arbolB.insertar(13);
        arbolB.insertar(15);
        arbolB.insertar(17);
        // Mostrar el árbol B
        System.out.println("Árbol B después de la inserción:");
        arbolB.showBTree();
        System.out.println();
        // Buscar un nodo en el árbol
        int valorBuscado = 10;
        List<Integer> nodoEncontrado = arbolB.buscarNodo(valorBuscado);
        System.out.println("Nodo con valor " + valorBuscado + ": " + nodoEncontrado);
        System.out.println();
        // Eliminar un valor del árbol
        int valorEliminar = 6;
        arbolB.eliminar(valorEliminar);
        System.out.println("Árbol B después de eliminar el valor " + valorEliminar + ":");
        arbolB.showBTree();
        System.out.println();
        // Obtener el valor máximo del árbol
        int valorMaximo = arbolB.obtenerValorMaximo();
        System.out.println("Valor máximo del árbol: " + valorMaximo);
        System.out.println();
        // Obtener el nodo mínimo de la raíz
        NodoArbolB nodoMinimoRaiz = arbolB.obtenerNodoMinimoRaiz();
        System.out.print("Nodo mínimo de la raíz: ");
        nodoMinimoRaiz.imprimir();
        System.out.println();
    }
}
