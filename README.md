# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

### Detailed Architecture Diagram
![Detailed Sequence Diagram](https://github.com/shawcalvin/chess/assets/125940261/2b4b55c2-67b9-40b4-8eca-c80e12704b4c)
https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWOZVYSnfoccKQCLAwwAIIgQKAM4TMAE0HAARsAkoYMhZkwBzKBACu2GAGI0wKgE8YAJRRakEsFEFIIaYwHcAFkjAdEqUgBaAD4WakoALhgAbQAFAHkyABUAXRgAej0VKAAdNABvLMpTAFsUABoYXCl3aBlKlBLgJAQAX0wKcNgQsLZxKKhbe18oAAoiqFKKquUJWqh6mEbmhABKDtZ2GB6BIVFxKSitFDAAVWzx7Kn13ZExSQlt0PUosgBRABk3uCSYCamYAAxKzxACyf2ymFu+we2x6nT6KCiaD0CAQG2oWx28hhhxgIEGghQ50ol2KwDKlWqczqNxx9ykTzUCiiAEkAHLvKy-f4U6bU+aLZYtGAcpLxCG0BFY0LQhkSKIElBE4R6MCeMmTPl0vbypkvUWct7cyVaykwYBqzxJCAAa3QhvFFqtUPpB0egXhm36zvVNvtaAxiLhoQRkV91rt6CDlBD8GQ6DAUQATAAGVN5fKWv1RtDtdAyTQ6fSGIzQXjHGAfCB2NxGLw+PwJoKh1jhuKJVIZFRSFxoTO880CurtMPdVuYn0IGtINCaqZU2aC9bS8RMuXuo4nEljQcoHV3d36lnkT7fHlXPmLmp1IEg8ETV26o+eieIqJ7680hZBmUwDcPIqhK+Kq6rztqT6HrCPQGhyXIXuS5rZpGAaOhKyGQbiHpepOSIRv60arqor69OGyEEYGY5xv4iYpummbkbm+ZoIW2i6AYxg6CgDrVlo+jMPW3i+JgNEtqRUBRNEfBnkkbxpOkPYSH2eSMQGMbjr07BRNOfFqqMqnoCu3rEbKbqATAxxgKBGoGWgB5Ycerxnj8+G5neYIRpheokURUS2b+a7Ys+5kyCgCAnCg1n6VaFH2d5zwntJXyya5qHAh5GEAYyPnGciqLokRcZjnlaLqdRzZJjAaappgBZFuxpaDDIVbDDAADifKPIJjYiRVTLFTEbVvF26RaHyKkxbmZU4e+MDIA4HVlBI0U5gGRm4euZl4pZUW2XFL4JU5XwubZ7ngplW3YW+WmpYRxmbcFeLzWAi2SKM+3QYdp7Hb8Y1LWdFmdV5L4zTd+QwAARH9kgQ5Jz1HJ1aSjvdOVdFE4NQ51sMxPDgNLUj009KJlXVZmkPQxI2PRPkmNlKyfDYwAjMmADMAAslQQw2vg7lMsMQxDlR5BDCgIKAtq83y-OC+TfLsnyEQC60MApMxrHFhxRjYHoUDYOF8DAaor0eEJTYBMwM3tgkyTydDE2regmbQ-LZTI10-W5fihuvaMztXrddkBSZ-6XVuVlWitKGGcDn3Mkd54BwDF2PVdmk+v5hVBVBeJKkSPt+2UH3ZV9cHGr9cv+9D9OVNDaF45IMfZaDPpV3wQdFW2sAI3TbdUSRxN0RmaCt7VLH1SWxjmGF07uDAABSECzu1fLGKL4u9ebHtozEsSnCNdumJNAaZsTcAQNOUA13y9NuzQGm+V7yq+D7IAVWfF9Xz3lR7e3WdYWHu0j7Ryyh6L67wfqJ3SudF0ICO64T8kAyiKNTIpzDvna+fAi6gLjt9BOtcoH12kLA1Gs1oa-xQdnBUfxsByGfnyX2GDKiv3Nu-aAWDHIwFOLEPgwgUrc2JJec0a8QAS0EaoPIHJ64Awpo3VOD8USlUzpbCSMAFEFU7uVc2g9R7qwapxJwlhEDKlgMAbAetCDOFcCbHqxMt530kklYackMjqGmtdH0r9jG3CUCod65CQ6oLUGFCKUV2EwUSjJN4popgSG-ogx4BDk6ULgbNNR-iQFRFCuFHm2RlphK+o4lKe5YkzBvAsEpwoEAJPvKaIhl0Uk3TSZnCh-8gnZJQK9PJsiOGFKia3T+KAXbTH4ZLIRYsRGjOmPgmpMjiHNzwk0lGyiSrqPdv3Cq2i6pAA

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
