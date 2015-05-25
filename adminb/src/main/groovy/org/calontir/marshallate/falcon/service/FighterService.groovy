package org.calontir.marshallate.falcon.service

import groovyx.gaelyk.GaelykBindings
import org.calontir.marshallate.falcon.common.FighterStatus

/**
 *
 * @author rik
 */
@GaelykBindings
class FighterService {
    def fighterCount() {
        def q = "select count from Fighter where status != ${FighterStatus.DELETED.toString()}"

        println q
        datastore.execute { q }
    }
}
