package Ejercicios
class TiposDeDatos{
    fun Datos(){
        //ENTEROS
        var age0: Byte = 18
        var age1: Int = 18
        var age2: Short = 18
        var age3: Long = 18 // Explicitly define variable type
        var age4 = 18L // Implicit define variable type
        val intRange = 1..4 // 1, 2, 3, 4

        val weight = 52 // Int inferred
        val healthy = 50..75
        if (weight in healthy)
            println("$weight is in $healthy range") //Prints: 52 is in 50..75 range
    }
}
