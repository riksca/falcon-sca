
import groovy.xml.MarkupBuilder

logger.ReportsReport.info "Reports Report"

def fighterInfo = datastore.iterate {
    select all from Fighter
}

StringWriter writer = new StringWriter()
def build = new MarkupBuilder(writer)
build.html {
    body {
        fighterInfo.each {
            logger.ReportsReport.info "${it.key.id}  ${it.scaName}"
            p"${it.key.id}  ${it.scaName}"
        }
    }
}

println writer.toString()