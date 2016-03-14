package pl.lantkowiak.sdm

import org.scalatest.{Matchers, FlatSpec}

class ExampleScalaUnitTest extends FlatSpec with Matchers {
  "addition" should "be correct" in {
    2 + 2 should be(4)
  }
}