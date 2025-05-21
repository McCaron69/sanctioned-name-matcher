# Sanctioned Name Matcher

Simple Spring Boot service to check if a given name matches any sanctioned name.

## How it works

- Normalizes input (removes noise words, punctuation, sorts tokens)
- Matches against known names using string similarity algorithms (Levenshtein, Jaro-Winkler, Jaccard)
- Returns match result with score

## Tech stack

Java 21, Spring Boot, Apache Commons Text, OpenAPI, H2

## Run locally

```bash
./gradlew bootRun
```

Swagger UI:  
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Example

**POST** `/check`

Request:
```json
{ "name": "Bin Laden, Osama" }
```

Response:
```json
{
  "match": true,
  "inputName": "Bin Laden, Osama",
  "normalizedInput": "bin laden osama",
  "matchedName": "Osama Bin Laden",
  "similarityScore": 1.0
}
```

## Preloaded Sanctioned Names

When the application starts, the following names are preloaded into the database:

- Osama Bin Laden
- Ali Baba
- Joe Smith
- Ben Ladin
- Robert Johnson
