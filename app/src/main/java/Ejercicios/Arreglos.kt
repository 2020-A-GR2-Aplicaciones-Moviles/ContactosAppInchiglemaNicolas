package Ejercicios
class Arreglos{
    fun arreglos(){
        val array = arrayOf(1,2,3) // inferred type Array<Int>
        println(array.get(0)) //Get position
        println(array.size) //Get size
        array.set(2,5)//Change value of index 2, 3 to 5
        println(array[1]) //Prints: 2

        val array2: Array<Short> = arrayOf(1,2,3)
        val array3: Array<Long> = arrayOf(1,2,3)
        val array4: Array<Int?> = arrayOfNulls(3) // Prints: [null, null, null]
        val array5 = Array (5) { it * 2 } // Prints: [0, 2, 4, 8, 10]

        var pair = "Everest" to 8848 // Inferred type: Pair<String, Int>
    }
}
