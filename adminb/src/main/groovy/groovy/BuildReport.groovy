import org.calontir.marshallate.falcon.user.Security
import org.calontir.marshallate.falcon.user.SecurityFactory
import org.joda.time.DateTime
import groovy.xml.MarkupBuilder
import com.google.appengine.api.datastore.*
import static com.google.appengine.api.datastore.FetchOptions.Builder.*
import org.calontir.marshallate.falcon.db.FighterDAO
import org.calontir.marshallate.falcon.dto.Fighter
import org.apache.commons.lang.StringEscapeUtils
import org.calontir.marshallate.falcon.common.*
import com.google.appengine.api.ThreadManager

Thread thread = ThreadManager.createBackgroundThread(new Runnable() {
        public void run() {
            def now = new DateTime()
            FighterDAO dao = new FighterDAO()
            def kingdom = "Calontir"
            Fighter user = dao.getFighterByGoogleId(params["user.googleid"])
            if(user == null) {
                logger.BuildReport.error "Report failed no user found for "
                + params["user.googleId"]
                return // no
            }
            logger.BuildReport.info "Generating report for ${user.scaName}"
            def ccs = params["Email Cc"]?.split(",")
            if(!ccs) {
                ccs = []
            }
            ccs += user?.email[0].emailAddress
            def from
            def to
            def location = user?.scaGroup?.groupLocation
            def activities = StringEscapeUtils.unescapeHtml(params["Activities"])
            def rmType = params["Reporting Marshal Type"]
            def rmt = ReportingMarshalType.getByCode(rmType)
            if (rmt == null) {
                rmt = ReportingMarshalType.ARMORED_COMBAT
            }

            namespace.of("system") {
                def query = new Query("properties")
                query.addFilter("name", Query.FilterOperator.EQUAL,
                    kingdom.toLowerCase() + ".earlmarshal.email")
                PreparedQuery preparedQuery = datastore.prepare(query)
                def entities = preparedQuery.asList( withLimit(10) )
                if(entities != null && entities.size() > 0) {
                    def entity = entities[0]
                    ccs += entity.property
                }

                if (rmt.equals(ReportingMarshalType.CALON_STEEL)) {
                    query = new Query("properties")
                    query.addFilter("name", Query.FilterOperator.EQUAL,
                        kingdom.toLowerCase() + ".calonsteel.email")
                    preparedQuery = datastore.prepare(query)
                    entities = preparedQuery.asList( withLimit(10) )
                    if(entities != null && entities.size() > 0) {
                        def entity = entities[0]
                        ccs += entity.property
                    }
                }

                query = new Query("properties")
                query.addFilter("name", Query.FilterOperator.EQUAL,
                    kingdom.toLowerCase() + ".from.email")
                preparedQuery = datastore.prepare(query)
                entities = preparedQuery.asList( withLimit(10) )
                if(entities != null && entities.size() > 0) {
                    def entity = entities[0]
                    from = entity.property
                }

                query = new Query("properties")
                query.addFilter("name", Query.FilterOperator.EQUAL,
                    kingdom.toLowerCase() + "." + location?.toLowerCase() + ".email")
                preparedQuery = datastore.prepare(query)
                entities = preparedQuery.asList( withLimit(10) )
                if(entities != null && entities.size() > 0) {
                    def entity = entities[0]
                    to = entity.property
                }
            }

            namespace.of(kingdom.toLowerCase()) {
                def reportInfo = new Entity ("Report")
                reportInfo.dateEntered = now.toString()
                reportInfo.reportType = params["Report Type"]
                reportInfo.marshalName = user?.scaName
                reportInfo.marshalId = user?.fighterId
                reportInfo.googleId = user?.googleId

                def reportInfoId = reportInfo.save()

                params.keySet().each {
                    def reportParams = new Entity("ReportParams")
                    reportParams.reportKey = reportInfoId
                    reportParams.name = it
                    reportParams.value = params[it]

                    reportParams.save()
                }
            }

            StringWriter writer = new StringWriter()
            def build = new MarkupBuilder(writer)
            build.html{
                head{
                    title('Marshal Report')
                    style(type:"text/css", '''
                    .sect_title {
                        text-decoration:underline;
                    }
                    .sect_body {
                    }
                    ''')
                }
                body {
                    h1 "Marshal Report"

                    p {
                        h3 ('class':'sect_title', 'style':'display: inline;', "Reporting Marshal Type: " )
                        span ('class':'sect_body', rmt.value)
                    }

                    p {
                        h3 ('class':'sect_title', 'style':'display: inline;', "Reporting Period: " )
                        span ('class':'sect_body', params["Report Type"])
                    }

                    if (rmt.equals(ReportingMarshalType.ARMORED_COMBAT)) {
                        p {
                            h3 ('class':'sect_title', 'style':'display: inline;',  "Marshal Type: "  )
                            span ('class':'sect_body', params["Marshal Type"])
                        }
                    }

                    if("Event".equals(params["Report Type"])) {
                        p {
                            h3 ('class':'sect_title', 'style':'display: inline;',  "Event Name: "  )
                            span ('class':'sect_body', params["Event Name"])
                        }
                        p {
                            h3 ('class':'sect_title', 'style':'display: inline;',  "Event Date: "  )
                            span ('class':'sect_body', params["Event Date"])
                        }
                    }

                    p {
                        h3 ('class':'sect_title', 'style':'display: inline;',  "SCA Name: "  )
                        span ('class':'sect_body', params["SCA Name"])
                    }

                    p {
                        h3 ('class':'sect_title', 'style':'display: inline;',  "Modern First & Last Name: "  )
                        span ('class':'sect_body', 'style':'display: inline;', params["Modern Name"])
                    }

                    p {
                        h3 ('class':'sect_title', 'style':'display: inline;',  "Address: "  )
                        span ('class':'sect_body', params["Address"])
                    }

                    p {
                        h3 ('class':'sect_title', 'style':'display: inline;',  "Phone Number: "  )
                        span ('class':'sect_body', params["Phone Number"])
                    }

                    p {
                        h3 ('class':'sect_title', 'style':'display: inline;',  "Membership Number: "  )
                        span ('class':'sect_body', params["SCA Membership No"])
                    }

                    p {
                        h3 ('class':'sect_title', 'style':'display: inline;',  "Membership Expires: "  )
                        span ('class':'sect_body', params["Membership Expires"])
                    }

                    p {
                        h3 ('class':'sect_title', 'style':'display: inline;',  "Home Group: "  )
                        span ('class':'sect_body', params["Group"])
                    }

                    if(params["Active Fighters"]) {
                        p {
                            h3 ('class':'sect_title', 'style':'display: inline;',  "Number of Authorized Fighters: "  )
                            span ('class':'sect_body', params["Active Fighters"])
                        }
                    }

                    if(params["Minor Fighters"]) {
                        p {
                            h3 ('class':'sect_title', 'style':'display: inline;',  "Number of Minors: "  )
                            span ('class':'sect_body', params["Minor Fighters"])
                        }
                    }

                    if(params["Activities"]) {
                        h3 ('class':'sect_title',  "Activities: "  )
                        p { mkp.yieldUnescaped params["Activities"] }
                    }

                    if(params["Injury"]) {
                        h3 ('class':'sect_title',  "Problems or Injuries: "  )
                        p { mkp.yieldUnescaped params["Injury"]}
                    }

                    if(params["Fighter Comments"]) {
                        h3 ('class':'sect_title',  "Fighter Comments "  )
                        p {mkp.yieldUnescaped params["Fighter Comments"]}
                    }

                    if(params["Summary"]) {
                        h3 ('class':'sect_title',  "Summary: "  )
                        p {mkp.yieldUnescaped params["Summary"]}
                    }
                }
            }

            logger.BuildReport.info "Sending report for ${params["Report Type"]}: to ${to}, from ${from}, cc ${ccs}"

            mail.send from: from,
            to: to,
            cc: ccs,
            subject: "Marshal report - ${user.scaName} - ${params["Report Type"]}",
            htmlBody: writer.toString()

        }
    });
thread.start()

