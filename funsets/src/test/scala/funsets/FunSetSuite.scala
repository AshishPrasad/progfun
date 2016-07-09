package funsets

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

/**
  * This class is a test suite for the methods in object FunSets. To run
  * the test suite, you can either:
  * - run the "test" command in the SBT console
  * - right-click the file in eclipse and chose "Run As" - "JUnit Test"
  */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  /**
    * Link to the scaladoc - very clear and detailed tutorial of FunSuite
    *
    * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
    *
    * Operators
    * - test
    * - ignore
    * - pending
    */

  /**
    * Tests are written using the "test" operator and the "assert" method.
    */
  // test("string take") {
  //   val message = "hello, world"
  //   assert(message.take(5) == "hello")
  // }

  /**
    * For ScalaTest tests, there exists a special equality operator "===" that
    * can be used inside "assert". If the assertion fails, the two values will
    * be printed in the error message. Otherwise, when using "==", the test
    * error message will only say "assertion failed", without showing the values.
    *
    * Try it out! Change the values so that the assertion fails, and look at the
    * error message.
    */
  // test("adding ints") {
  //   assert(1 + 2 === 3)
  // }


  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
    * When writing tests, one would often like to re-use certain values for multiple
    * tests. For instance, we would like to create an Int-set and have multiple test
    * about it.
    *
    * Instead of copy-pasting the code for creating the set into every test, we can
    * store it in the test class using a val:
    *
    * val s1 = singletonSet(1)
    *
    * However, what happens if the method "singletonSet" has a bug and crashes? Then
    * the test methods are not even executed, because creating an instance of the
    * test class fails!
    *
    * Therefore, we put the shared values into a separate trait (traits are like
    * abstract classes), and create an instance inside each test method.
    *
    */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
  }

  /**
    * This test is currently disabled (by using "ignore") because the method
    * "singletonSet" is not yet implemented and the test would fail.
    *
    * Once you finish your implementation of "singletonSet", exchange the
    * function "ignore" by "test".
    */
  test("singletonSet(1) contains 1") {

    /**
      * We create a new instance of the "TestSets" trait, this gives us access
      * to the values "s1" to "s3".
      */
    new TestSets {
      /**
        * The string argument of "assert" is a message that is printed in case
        * the test fails. This helps identifying which assertion failed.
        */
      assert(contains(s1, 1), "Singleton")
    }
  }

  test("union contains all elements of each set") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("intersection contains common elements from each set") {
    new TestSets {
      val u12 = union(s1, s2)
      val is1s2 = intersect(s1, s2)
      val iu12s2 = intersect(u12, s2)
      assert(!contains(is1s2, 1), "Intersect 1")
      assert(!contains(is1s2, 2), "Intersect 2")
      assert(contains(iu12s2, 2), "Intersect 3")
    }
  }

  test("diff contains elements from a set not in another set") {
    new TestSets {
      val u12 = union(s1, s2)
      val ds1s2 = diff(s1, s2)
      val du12s3 = diff(u12, s3)
      assert(contains(ds1s2, 1), "Diff 1")
      assert(!contains(ds1s2, 2), "Diff 2")
      assert(contains(du12s3, 1), "Diff 3")
      assert(contains(du12s3, 2), "Diff 4")
      assert(!contains(du12s3, 3), "Diff 5")
    }
  }

  test("filter contains elements from a set which follow a predicate") {
    new TestSets {
      val u12 = union(s1, s2)
      val u123 = union(u12, s3)
      val fs1s2 = filter(s1, s2)
      val fu12s3 = filter(u12, s3)
      val fu123s3 = filter(u123, s3)
      assert(!contains(fs1s2, 1), "Filter 1")
      assert(!contains(fs1s2, 2), "Filter 2")
      assert(!contains(fu12s3, 1), "Filter 3")
      assert(!contains(fu12s3, 2), "Filter 4")
      assert(!contains(fu12s3, 3), "Filter 5")
      assert(!contains(fu123s3, 1), "Filter 6")
      assert(!contains(fu123s3, 2), "Filter 7")
      assert(contains(fu123s3, 3), "Filter 8")
    }
  }

  test("map contains elements from a set") {
    new TestSets {
      val ms1 = map(s1, (x => 2 * x))
      assert(contains(ms1, 2), "Map 1")
      assert(!contains(ms1, 1), "Map 2")
    }
  }

  test("for all") {
    new TestSets {
      val u123 = union(union(s1, s2), s3)
      val i123 = intersect(intersect(s1, s2), s3)
      val fa1 = forall(u123, (x => s1(x) | s2(x) | s3(x)))
      assert(fa1, "For All 1")
      val fa2 = forall(u123, (x => i123(x)))
      assert(!fa2, "For All 2")
    }
  }

  test("exists") {
    new TestSets {
      val u123 = union(union(s1, s2), s3)
      val i123 = intersect(intersect(s1, s2), s3)
      val e1 = exists(u123, (x => s1(x)))
      assert(e1, "Exists 1")
      val e2 = exists(u123, (x => s2(x)))
      assert(e2, "Exists 2")
      val e3 = exists(u123, (x => s3(x)))
      assert(e3, "Exists 3")
      val e4 = exists(u123, (x => i123(x)))
      assert(!e4, "Exists 4")
    }
  }

}
