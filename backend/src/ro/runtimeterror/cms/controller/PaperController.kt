package ro.runtimeterror.cms.controller

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ro.runtimeterror.cms.database.DatabaseSettings
import ro.runtimeterror.cms.database.daos.PaperDAO
import ro.runtimeterror.cms.database.daos.UserDAO
import ro.runtimeterror.cms.database.tables.PaperTable
import ro.runtimeterror.cms.database.tables.UserTable
import ro.runtimeterror.cms.model.Paper
import ro.runtimeterror.cms.repository.Repository

class PaperController(private val repository: Repository)
{
    /**
     * Get all papers
     */
    fun getPapers(): List<Paper>
    {
        var listOfPapers: List<Paper> = ArrayList<Paper>()
        transaction (DatabaseSettings.connection){
            listOfPapers = PaperDAO.all().toList()
        }
        return listOfPapers
    }

    /**
     * Author submitted a paper
     */
    fun submitProposal(
        field: String,
        proposalName: String,
        keywords: String,
        topics: String,
        listOfAuthors: String,
        userId: Int
    )
    {
//        TODO I added userID to the paper class not sure if that's okay or not
//        checks if the user exists
        transaction(DatabaseSettings.connection) {
            if(
                UserDAO.find{
                    UserTable.id eq userId
                }.empty()
            ) {
                throw RuntimeException("User does not exist!")
            }
//            Checks if the user already as a paper
            else if(
                        !PaperDAO.find{
                            PaperTable.userid eq userId
                        }.empty()
                    ){
                throw RuntimeException("User already has a paper!")
            }
        }
        //adds paper to the paper table
        SchemaUtils.create(PaperTable)

        transaction(DatabaseSettings.connection) {
            PaperTable.insert{ newPaper ->
                newPaper[userid] = userId
                newPaper[PaperTable.field] = field
                newPaper[name] = proposalName
                newPaper[PaperTable.keywords] = keywords
                newPaper[PaperTable.topics] = topics
                newPaper[PaperTable.topics] = topics
                newPaper[authors] = listOfAuthors
                newPaper[accepted] = false
                newPaper[conflicting] = false

            }
        }
    }

    fun fullPaperUploaded(path: String, userId: Int)
    {
        TODO("Not yet implemented")
    }

}
