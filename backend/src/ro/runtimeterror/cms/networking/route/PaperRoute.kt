package ro.runtimeterror.cms.networking.route

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondFile
import io.ktor.routing.*
import ro.runtimeterror.cms.controller.PaperSubmissionController
import ro.runtimeterror.cms.exceptions.ProgramException
import ro.runtimeterror.cms.model.UserType
import ro.runtimeterror.cms.networking.authorize
import ro.runtimeterror.cms.networking.dto.AbstractDTO
import ro.runtimeterror.cms.networking.dto.CreatePaperDTO
import ro.runtimeterror.cms.networking.dto.toDTO
import ro.runtimeterror.cms.networking.uploadFile
import ro.runtimeterror.cms.networking.userSession
import java.io.File


fun Route.paperSubmissionRoute(paperSubmissionController: PaperSubmissionController)
{
    route("/paper") {
        get {
            authorize(UserType.AUTHOR)
            val user = userSession()
            val papers = paperSubmissionController.getPapers(user.id)
            call.respond(papers.toDTO())
        }

        post {
            authorize(UserType.AUTHOR)
            val paper = call.receive<CreatePaperDTO>()
            val user = userSession()
            with(paper)
            {
                paperSubmissionController.submitProposal(
                    user.id,
                    name,
                    field,
                    keywords,
                    topics,
                    abstract,
                    authors
                )
            }
            call.respond(HttpStatusCode.OK)
        }
        put {
            authorize(UserType.AUTHOR)
            val user = userSession()
            val abstract = call.receive<AbstractDTO>()
            paperSubmissionController.changeAbstract(user.id, abstract.paperId, abstract.abstract)
            call.respond(HttpStatusCode.OK)
        }

        put("/full/{paperId}") {
            authorize(UserType.AUTHOR)
            val user = userSession()
            val paperId = call.parameters["paperId"]?.toInt() ?: throw ProgramException("Specify the paper id")
            val path = uploadFile(user.id)
            paperSubmissionController.uploadFullPaper(path, paperId, user.id)
            call.respond(HttpStatusCode.OK)
        }
    }
}