Feature:

  Background:
    * url "http://localhost:8080/upload"


  Scenario:
    * def fileName = "text.txt"
    * def contentType = "text/plain"

    Given multipart file file = ({read: fileName, filename: fileName, 'Content-Type': contentType})

    # this line has no effect
    * karate.configure('charset',null)

    When method POST
    Then status 200
