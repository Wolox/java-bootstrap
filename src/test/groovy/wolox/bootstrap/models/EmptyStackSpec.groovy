package wolox.bootstrap.models

import spock.lang.Specification

class EmptyStackSpec extends Specification {
    def stack = new Stack()

    def "size"() {
        expect: stack.size() == 0
    }

    def "pop"() {
        when: stack.pop()
        then: thrown(EmptyStackException)
    }

    def "peek"() {
        when: stack.peek()
        then: thrown(EmptyStackException)
    }

    def "push"() {
        when:
        stack.push("elem")

        then:
        stack.size() == old(stack.size()) + 1
        stack.peek() == "elem"
    }
}
