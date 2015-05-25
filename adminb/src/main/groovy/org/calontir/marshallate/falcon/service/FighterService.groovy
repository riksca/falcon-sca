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
        datastore.execute {
            select count from Fighter where status != 'DELETED'
        }
    }
}
