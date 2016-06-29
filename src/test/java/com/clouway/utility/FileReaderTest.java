package com.clouway.utility;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class FileReaderTest {

  @Test
  public void readFile() {
    FileReader reader = new FileReader();
    String fileContent = reader.read("src/test/java/com/clouway/utility/test.txt");

    assertThat(fileContent, is(equalTo("This is for testing FileReader.")));
  }

  @Test
  public void readAnotherFile() {
    FileReader reader = new FileReader();
    String fileContent = reader.read("src/test/java/com/clouway/utility/test2.txt");

    assertThat(fileContent, is(equalTo("This is another test.")));
  }

  @Test
  public void readNonExistingFile() {
    FileReader reader = new FileReader();
    String fileContent = reader.read("src/test/java/com/clouway/utility/test3.txt");

    assertThat(fileContent, is(equalTo("404 File not found!")));
  }

}