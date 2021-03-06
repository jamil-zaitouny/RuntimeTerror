package ro.runtimeterror.cms.networking.route

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.put
import io.ktor.routing.route
import ro.runtimeterror.cms.controller.PaperBidController
import ro.runtimeterror.cms.model.UserType
import ro.runtimeterror.cms.networking.authorize
import ro.runtimeterror.cms.networking.dto.BidDTO
import ro.runtimeterror.cms.networking.dto.toPaperBidDTO
import ro.runtimeterror.cms.networking.userSession

fun Route.paperBidRoute(paperBidController: PaperBidController)
{
    route("/paper/bid") {
        get {
            authorize(UserType.PC_MEMBER)
            val user = userSession()
            val papers = paperBidController.getPapers(user.id)
            call.respond(papers.toPaperBidDTO())
        }

        put {
            authorize(UserType.PC_MEMBER)
            val user = userSession()
            val bidDTO = call.receive<BidDTO>()
            paperBidController.bid(user.id, bidDTO.paperId, bidDTO.bidResult)
            call.respond(HttpStatusCode.OK)
        }
    }
}