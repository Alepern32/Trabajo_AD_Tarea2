# Trabajo_AD_Tarea2

## Trabajo de Acceso a datos

Comparativa: Código que Cuenta Bien vs Código que Cuenta Mal
CÓDIGO QUE CUENTA MAL
Problema principal: Lee y escribe en dos operaciones separadas.
Primero lee el valor de la base de datos, luego lo modifica en Java y finalmente lo guarda. Entre la lectura y la escritura, otro programa puede haber cambiado el valor, perdiéndose actualizaciones.
No usa transacciones, por lo que cada operación es independiente y no hay control sobre ejecuciones simultáneas.
Cada incremento requiere dos viajes a la base de datos: uno para leer y otro para escribir, lo que es ineficiente y propenso a errores.
Si ejecutas tres programas a la vez, en lugar de llegar a 3000, probablemente llegues a 1500-2000 porque se pierden actualizaciones.
CÓDIGO QUE CUENTA BIEN
Solución principal: Usa una única operación atómica en la base de datos.
Ejecuta un solo comando UPDATE que incrementa directamente el valor en la base de datos, sin necesidad de leerlo primero.
Utiliza transacciones para agrupar múltiples operaciones, asegurando que todas se completen o ninguna se aplique.
Controla manualmente cuándo confirmar los cambios, previniendo que otros programas vean estados intermedios.
Si ejecutas tres programas a la vez, siempre llegará exactamente a 3000 porque cada incremento es seguro contra interferencias.
Diferencia Clave
El código malo trabaja fuera de la base de datos y sufre condiciones de carrera, mientras que el código bueno delega el trabajo a la base de datos donde las operaciones son atómicas y seguras para concurrencia.
