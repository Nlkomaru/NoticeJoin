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
    val connection: Connection = DriverManager.getConnection("jdbc:sqlite:Any Directory${File.separator}NoticeConnect.db")
    val path = "The path to the server's world directory ${File.separator}playerdata${File.separator}"
    val dir: Path = Path.of(path)
    try {
        val preparedStatement: PreparedStatement =
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS JoinedPlayerList (UUID VARCHAR(40) NOT NULL)")
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

## Config

```hocon
database {
    database = NoticeConnect
}
discord {
    ## The message that will be sent to the discord channel.
    message {
        firstJoin = "%(PlayerName) さんがサーバーに初参加です"
        join = "%(PlayerName) さんがサーバーに参加しました"
        left = "%(PlayerName) さんがサーバーから退出しました"
    }
    url = "https://discord.com/api/webhooks/*********/**************"
}
message {
    ## The message that will be sent to players on the server, randomly selected from an array
    ## Tag <name> = PlayerName  <currentServerName> = The name of the server to connect to ("lobby", "main", etc.).
    firstJoin = [
        "<#ec407a><name></#ec407a>さんがはじめてサーバーにやってきました！"
        
    ]
    join = [
        "<#9ccc65><name></#9ccc65>がサーバーにやってきました！"
        "<#9ccc65><name></#9ccc65>さんがサーバーに参加しました！"
    ]
    left = [
        "<#03a9f4><name></#03a9f4>さんがサーバーから退出しました！"
        "<#03a9f4><name></#03a9f4>、またね～"
    ]
}
## The mode of the message that will be sent to players on the server. (ActionBar, Chat, MainTitle, SubTitle)
mode = ActionBar
## The replacement for the message that will be sent to players on the server
replace {
    lobby = "ロビー"
    main = "生活"
    res = "資源"
}
## Config Version Dont touch this
version = "2.0.0"
```
