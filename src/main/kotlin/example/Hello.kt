package example

import com.zaxxer.hikari.HikariDataSource

fun main(args: Array<String>) {
    val ds = HikariDataSource()
    ds.jdbcUrl = "jdbc:h2:mem:test"
    ds.username = "sa"
    ds.password = "sa"

    ds.connection.use { conn ->
        conn.createStatement().use { stmt ->
            with(stmt) {
                executeUpdate("create table mytbl(id int primary key, name varchar(255))")
                executeUpdate("insert into mytbl values(1, 'Hello')")
                executeUpdate("insert into mytbl values(2, 'World')")
            }
        }

        conn.createStatement().use { stmt ->
            val rs = stmt.executeQuery("select * from mytbl")
            while (rs.next()) {
                println("> " + rs.getString("name"))
            }
        }
    }
}

fun <T : AutoCloseable?, R> T.use(block: (T) -> R): R {
    try {
        return block(this)
    } finally {
        try {
            this?.close()
        } catch (e: Exception) {
            println(e.toString())
        }
    }
}