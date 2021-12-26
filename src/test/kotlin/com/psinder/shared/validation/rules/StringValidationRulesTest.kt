package com.psinder.shared.validation.rules

import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.arrow.isNone
import strikt.arrow.isSome

class StringValidationRulesTest : DescribeSpec({

    describe("string validation rules") {

        describe("non empty") {

            it("fail when string is empty") {
                val rule = StringValidationRules.nonEmptyRule()

                expectThat(rule.check(""))
                    .isSome()
            }

            it("pass when contains only space") {
                val rule = StringValidationRules.nonEmptyRule()

                expectThat(rule.check(" "))
                    .isNone()
            }

            it("pass when contains non whitespace character") {
                val rule = StringValidationRules.nonEmptyRule()

                expectThat(rule.check("A"))
                    .isNone()
            }
        }

        describe("non blank") {

            it("fail when string is empty") {
                val rule = StringValidationRules.nonBlankRule()

                expectThat(rule.check(""))
                    .isSome()
            }

            it("fail when contains only space") {
                val rule = StringValidationRules.nonBlankRule()

                expectThat(rule.check(" "))
                    .isSome()
            }

            it("pass when contains non whitespace character") {
                val rule = StringValidationRules.nonBlankRule()

                expectThat(rule.check("A"))
                    .isNone()
            }
        }

        describe("min length") {

            it("fail when less") {
                val rule = StringValidationRules.minLengthRule(6)

                expectThat(rule.check("12345"))
                    .isSome()
            }

            it("pass when equal") {
                val rule = StringValidationRules.minLengthRule(6)

                expectThat(rule.check("123456"))
                    .isNone()
            }

            it("pass when more") {
                val rule = StringValidationRules.minLengthRule(6)

                expectThat(rule.check("1234567"))
                    .isNone()
            }
        }

        describe("contains number") {

            it("fail when no number") {
                val rule = StringValidationRules.containsNumberRule()

                expectThat(rule.check("no_number"))
                    .isSome()
            }

            it("pass when starts with number") {
                val rule = StringValidationRules.containsNumberRule()

                expectThat(rule.check("1_with_number"))
                    .isNone()
            }

            it("pass when ends with number") {
                val rule = StringValidationRules.containsNumberRule()

                expectThat(rule.check("with_number_1"))
                    .isNone()
            }

            it("pass when number in the middle") {
                val rule = StringValidationRules.containsNumberRule()

                expectThat(rule.check("with_1_number"))
                    .isNone()
            }
        }

        describe("contains special character") {

            it("fail when no special character") {
                val rule = StringValidationRules.containsSpecialCharacterRule()

                expectThat(rule.check("noSpecialCharacter"))
                    .isSome()
            }

            it("number is not treated as special character") {
                val rule = StringValidationRules.containsSpecialCharacterRule()

                expectThat(rule.check("1noSpecialCharacter"))
                    .isSome()
            }

            it("whitespace is not treated as special character") {
                val rule = StringValidationRules.containsSpecialCharacterRule()

                expectThat(rule.check("\t"))
                    .isSome()
            }

            it("pass when starts with special character") {
                val rule = StringValidationRules.containsSpecialCharacterRule()

                expectThat(rule.check("#WithSpecialCharacter"))
                    .isNone()
            }

            it("pass when ends with special character") {
                val rule = StringValidationRules.containsSpecialCharacterRule()

                expectThat(rule.check("withSpecialCharacter#"))
                    .isNone()
            }

            it("pass when special character in the middle") {
                val rule = StringValidationRules.containsSpecialCharacterRule()

                expectThat(rule.check("with#SpecialCharacter"))
                    .isNone()
            }
        }

        describe("cannot starts with") {

            it("fail when starts with given character") {
                val rule = StringValidationRules.cannotStartWithRule("A")

                expectThat(rule.check("ABC"))
                    .isSome()
            }

            it("can use more that single character as condition") {
                val rule = StringValidationRules.cannotStartWithRule("AB")

                expectThat(rule.check("ABC"))
                    .isSome()
            }

            it("pass when starts with different character") {
                val rule = StringValidationRules.cannotStartWithRule("A")

                expectThat(rule.check("BC"))
                    .isNone()
            }

            it("pass when matches only partially") {
                val rule = StringValidationRules.cannotStartWithRule("AB")

                expectThat(rule.check("ACB"))
                    .isNone()
            }
        }

        describe("cannot ends with") {

            it("fail when ends with given character") {
                val rule = StringValidationRules.cannotEndsWithRule("A")

                expectThat(rule.check("BCA"))
                    .isSome()
            }

            it("can use more that single character as condition") {
                val rule = StringValidationRules.cannotEndsWithRule("AB")

                expectThat(rule.check("CAB"))
                    .isSome()
            }

            it("pass when starts with different character") {
                val rule = StringValidationRules.cannotEndsWithRule("A")

                expectThat(rule.check("AB"))
                    .isNone()
            }

            it("pass when matches only partially") {
                val rule = StringValidationRules.cannotEndsWithRule("CAB")

                expectThat(rule.check("AC"))
                    .isNone()
            }
        }

        describe("cannot have whitespace") {

            it("fail when starts with space") {
                val rule = StringValidationRules.cannotHaveWhitespacesRule()

                expectThat(rule.check(" BCA"))
                    .isSome()
            }

            it("fail when ends with space") {
                val rule = StringValidationRules.cannotHaveWhitespacesRule()

                expectThat(rule.check("BCA "))
                    .isSome()
            }

            it("fail when contains space") {
                val rule = StringValidationRules.cannotHaveWhitespacesRule()

                expectThat(rule.check("CA BC"))
                    .isSome()
            }

            it("fail when contains tab") {
                val rule = StringValidationRules.cannotHaveWhitespacesRule()

                expectThat(rule.check("AB \t BA"))
                    .isSome()
            }

            it("fail when contains non-break space") {
                val rule = StringValidationRules.cannotHaveWhitespacesRule()

                expectThat(rule.check("AC \u00A0 CA"))
                    .isSome()
            }

            it("pass when no whitespace present") {
                val rule = StringValidationRules.cannotHaveWhitespacesRule()

                expectThat(rule.check("WITHOUT_WHITESPACES"))
                    .isNone()
            }
        }

        describe("valid email address") {

            it("should pass") {
                val rule = StringValidationRules.isValidEmailRule()

                expectThat(rule.check("joe@doe.com"))
                    .isNone()

                expectThat(rule.check("email@123.123.123.123"))
                    .isNone()
            }

            it("should fail") {
                val rule = StringValidationRules.isValidEmailRule()

                expectThat(rule.check("invalid_address"))
                    .isSome()
            }
        }
    }
})
