---
name: seedu-java-coding-standard
description: Apply and review the SE-EDU basic plus intermediate Java coding standard whenever creating, editing, refactoring, or reviewing Java code in this project.
---

# SE-EDU Java Coding Standard

Apply these rules to all production and test Java code in this repository. The authoritative source is the [SE-EDU Java coding standard (basic + intermediate)](https://se-education.org/guides/conventions/java/intermediate.html). For topics it does not cover, follow the Google Java Style Guide.

## Naming

- Use lowercase logical package names rooted at the project name.
- Use PascalCase nouns for classes and enums, camelCase verbs for methods, and camelCase for variables.
- Use SCREAMING_SNAKE_CASE for constants.
- Keep names in English. Write acronyms as ordinary words inside names, such as `exportHtml` rather than `exportHTML`.
- Give wide-scope variables descriptive names; reserve short names such as `i` for small-scope scratch values.
- Name booleans to read as booleans, preferably with prefixes such as `is`, `has`, `was`, `can`, or `should`.
- Use plural names for collections.
- Test methods may use `featureUnderTest_testScenario_expectedBehavior`.

## Layout

- Indent with 4 spaces and never tabs.
- Keep lines below 120 characters; aim for 110 or fewer.
- Indent wrapped continuation lines 8 spaces beyond the parent line. Break after commas and before operators when practical.
- Use K&R braces. Always use braces for loop and conditional bodies, including single statements.
- Indent `case` and `default` one level inside `switch`; indent their statements one further level.
- Mark every intentional colon-style switch fallthrough with `// Fallthrough`.
- Put spaces around operators and after keywords, commas, and `for` semicolons.
- Separate distinct logical units within a block with one blank line.

## Packages, imports, types, and variables

- Put every class in a package.
- Group imports consistently, list each import explicitly, and remove unused imports. Do not use wildcard imports.
- Attach array brackets to the type, such as `String[] args`.
- Declare variables in the smallest practical scope and initialize them where declared when a valid value is available.
- Do not expose mutable class variables publicly; use encapsulation. Public constants are allowed.

## Comments and Javadoc

- Write comments in English using American spelling and avoid local slang.
- Write descriptive Javadoc for public classes and public methods, except where an inherited comment, a trivial getter/setter, or test context makes it redundant.
- Start Javadoc with a short third-person summary such as `Returns`, `Adds`, or `Creates`.
- Use `/**` on its own line, align each `*`, leave a blank line before tag sections, and punctuate tag descriptions.
- Provide either meaningful `@param` tags for every parameter or omit them all when every name is self-explanatory. Add `@return` and `@throws` when they clarify the contract.
- Indent comments with the code they describe. Prefer explaining intent and constraints over restating the code.

## Review workflow

When Java code changes:

1. Review the changed files against every section above, not only the rule that prompted the edit.
2. Check line lengths, tabs, wildcard imports, switch indentation/fallthrough, braces, naming, and public API Javadoc.
3. Apply the repository's JUnit coverage policy and run `./gradlew test`.
4. Run the recorded console UI tests required by `AGENTS.md` before handing work back.
