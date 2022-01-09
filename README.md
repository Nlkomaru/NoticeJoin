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
//Written in 2022  by Nikomaru <nikomaru@nikomaru.dev>
//
//To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.
//This software is distributed without any warranty.
//
//You should have received a copy of the CC0 Public Domain Dedication along with this software.
//If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.

fun main(args: Array<String>) {

    Class.forName("org.sqlite.JDBC")
    val connection: Connection =
        DriverManager.getConnection("jdbc:sqlite:Any Directory${File.separator}NoticeConnect.db")
    val path = "The path to the server's world directory ${File.separator}playerdata${File.separator}"
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

    var counter = 0
    var counter1 = 0
    try {
        Files.list(dir).use { stream ->
            stream.forEach { p: Path ->
                if (!p.toString().contains("dat_old")) {
                    val uuid = p.toString().replace(path, "").replace(".dat", "")
                    println(uuid)
                    val statement = connection.prepareStatement("INSERT into JoinedPlayerList (UUID) VALUES (?)")
                    statement.setString(1, uuid)
                    statement.execute()
                    statement.close()
                    counter++
                }
                counter1++
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    connection.close()
    println("$counter $counter1 players added to the database")

}
```
