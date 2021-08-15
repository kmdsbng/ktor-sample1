package com.example.dataaccess

import com.example.dataaccess.tables.MessageTable
import com.example.model.Message
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction

class MessageDao {
    // メッセージを追加する
    fun addMessage(message: Message) {
        transaction {
            MessageTable.insert {
                it[this.id] = message.id
                it[this.message] = message.message
            }
        }
    }

    // メッセージを取得する
    fun getMessage(id: String): Message? {
        var result: Message? = null
        transaction {
            result = MessageTable
                .select { MessageTable.id eq id }
                .map { convertToMessage(it) }
                .firstOrNull()
        }
        return result
    }

    fun getMessageFirst(): Message? {
        var resultMessage: Message? = null
        transaction {
            val conn = TransactionManager.current()
            val result = conn.exec("select id, message from messages") { rs ->
                val ary = mutableListOf<Message>()
                while (rs.next()) {
                    val message = Message(
                        id = rs.getString("id"),
                        message = rs.getString("message"),
                    )
                    ary.add(message)
                }
                ary
            }

            if (result != null) {
                resultMessage = result[0]
            }
        }

        return resultMessage
    }

    // messagesテーブルのレコードをMessageオブジェクトに変換する
    private fun convertToMessage(row: ResultRow): Message {
        return Message(
            id = row[MessageTable.id],
            message = row[MessageTable.message],
        )
    }
}
