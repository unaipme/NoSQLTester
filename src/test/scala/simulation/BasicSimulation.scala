package simulation

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {
    val httpConf = http
        .baseUrl("http://localhost:2426/")
    
        
    
}