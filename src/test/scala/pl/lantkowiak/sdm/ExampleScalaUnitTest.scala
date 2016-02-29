package pl.lantkowiak.sdm

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.scalatest.junit.{AssertionsForJUnit, JUnitRunner}

@RunWith(classOf[JUnitRunner])
class ExampleScalaUnitTest {
  @Test def addition_isCorrect(): Unit = {
    assertThat(2 + 2).isEqualTo(4);
  }
}