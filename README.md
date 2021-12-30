# NoticeConnect
A login message plugin that runs on Velocity



[![CC0](http://i.creativecommons.org/p/zero/1.0/88x31.png)](http://creativecommons.org/publicdomain/zero/1.0/)


To the extent possible under law,
[<span property="dct:title">Nikomaru</span>](https://github.com/Nlkomaru/)
has waived all copyright and related or neighboring rights to
<span property="dct:title">NoticeConnect</span>.
This work is published from:
<span property="vcard:Country" datatype="dct:ISO3166" content="JP" about="https://github.com/Nlkomaru/NoticeConnect">日本</span>.


## Transfar Hint
Please run it in your own kotlin environment.
You will also need the server directory and the database output location.


```Kotlin

fun main(args: Array<String>) {
  
    Class.forName("org.sqlite.JDBC")
    val connection: Connection = DriverManager.getConnection("jdbc:sqlite:Any directory\\NoticeConnect.db")
    val path = "Any directory\\purpur 1.18.1\\world\\advancements\\"
    val dir: Path = Path.of(path)
    try {
        val preparedStatement: PreparedStatement = connection.prepareStatement(
            "CREATE TABLE IF NOT EXISTS JoinedPlayerList (UUID VARCHAR(40) NOT NULL)"
        )
        preparedStatement.execute()
        preparedStatement.close()
    } catch (ex: SQLException) {
        ex.printStackTrace()
    }

    try {
        Files.list(dir).use { stream ->
            stream.forEach { p: Path ->
                val uuid = p.toString().replace(path, "").replace(".json","")
                println(uuid)
                val statement = connection.prepareStatement("INSERT into JoinedPlayerList (UUID) VALUES (?)")
                statement.setString(1, uuid)
                statement.execute()
                statement.close()
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
     connection.close()

}
```

This code is also licensed under cc0, and writer assumes no responsibility for it.
