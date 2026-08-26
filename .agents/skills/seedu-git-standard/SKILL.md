---
name: seedu-git-standard
description: Apply and review the SE-EDU Git conventions when planning branches or proposing, preparing, reviewing, or creating commits in this project.
---

# SE-EDU Git Standard

Apply these rules to Git branches and every commit in this repository. The authoritative source is the [SE-EDU Git conventions guide](https://se-education.org/guides/conventions/git.html).

This skill governs commit quality; it does not grant permission to commit, tag, merge, rewrite history, or push. Follow the authorization and safety rules in `AGENTS.md` before mutating Git state.

## Commit subject

- Write a meaningful subject for every commit.
- Aim for 50 characters or fewer and never exceed 72 characters.
- Use imperative mood, as if completing the phrase "This commit will ...".
- Capitalize the first letter.
- Do not end with a period.
- Add a concise `<scope>:` or `<category>:` prefix only when it improves clarity.

Good: `Add task storage tests`

Bad: `Added task storage tests.`

## Commit body

- Add a body for every non-trivial commit.
- Separate the subject and body with one blank line.
- Wrap body lines at 72 characters and separate paragraphs with blank lines.
- Explain what changes and why it is needed; leave implementation details to the diff.
- Describe the existing situation in present tense and describe the change in imperative mood.
- Use bullets when they make multiple points easier to read.
- Avoid redundant details already captured in code comments.
- Avoid filler such as `currently` and `originally` when the tense already makes it clear.
- Split the work into smaller focused commits if a clear body becomes excessively long or covers unrelated concerns.

## Branch names

- When choosing a branch name, use meaningful keywords in kebab-case, such as `refactor-ui-tests`.
- For issue-related work, prefer `issueNumber-keywords`, such as `1234-ui-freeze-error`.
- If the user explicitly provides a branch name, preserve that requested name unless it is unsafe or invalid.

## Commit workflow

Before creating a commit:

1. Confirm the user has explicitly authorized the commit.
2. Inspect `git status` and the staged diff. Stage only the intended files and preserve unrelated user changes.
3. Ensure the staged changes form one focused unit and have passed the project-required tests.
4. Draft the subject and body using the rules above; check the 50/72-character limits.
5. Create the commit without bypassing hooks or checks.
6. Verify the resulting commit message, commit contents, and worktree status.
