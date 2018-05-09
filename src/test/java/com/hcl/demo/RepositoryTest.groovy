package com.hcl.demo

import spock.lang.Specification

class RepositoryTest extends Specification {

    Repository repository = new Repository()

    def 'should be able to save and load credit card'() {
        given:
            UUID uuid = UUID.randomUUID()
        and:
            CreditCard card = new CreditCard(uuid)
        and:
            card.assignLimit(100)
        and:
            repository.save(card)
        when:
            CreditCard loaded = repository.load(uuid)
        then:
            loaded.availableLimit() == 100
    }
}
