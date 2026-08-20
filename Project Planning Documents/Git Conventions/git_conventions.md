# Git Conventions

This document defines how our team uses Git: how we name branches, write commits, open pull requests, and run common commands.

## Quick Summary

For most day-to-day work:

* Create branches using `<type>/<short-description>`, for example `feature/add-user-search`.
* Use clear commit messages such as `feat: add user search` or `fix: prevent duplicate submission`.
* Keep branches, commits, and pull requests focused on one logical change.
* Start work from the latest `master`.
* Review `git status` and `git diff` before committing.
* Keep your branch up to date before opening or merging a pull request.
* Use `git push --force-with-lease` instead of `git push --force` when rewriting a pushed branch.
* Do not commit secrets, credentials, `.env` files, or unrelated changes.
* Do not commit directly to protected branches.
* Delete branches after they have been merged.

Typical workflow:

```bash
git switch master
git pull
git switch -c feature/add-user-search

# Make changes

git status
git diff
git add .
git commit -m "feat: add user search"

git fetch origin
git rebase origin/master

git push -u origin feature/add-user-search
```

## Goals

Our Git conventions should make it easy to:

* Understand what a change is for
* Find related branches, commits, and pull requests
* Review changes quickly
* Keep history readable
* Avoid unnecessary merge conflicts
* Know which commands to run for common workflows

## Branch Naming

Use lowercase branch names with words separated by hyphens.

```text
<type>/<short-description>
```

### Branch types

```text
feature/
fix/
bugfix/
hotfix/
chore/
refactor/
docs/
test/
```

Examples:

```text
feature/add-user-search
fix/login-redirect
hotfix/payment-timeout
refactor/auth-service
docs/update-api-guide
chore/update-dependencies
```

### Rules

* Keep branch names short but descriptive.
* Use lowercase for the description.
* Use hyphens instead of spaces or underscores.
* Do not use your name as the branch name.
* Avoid vague names such as:

```text
fix-stuff
changes
test
new-branch
why-is-this-not-working
```

## Starting New Work

Start from the latest version of the team's base branch.

```bash
git checkout master
git pull
git checkout -b feature/add-user-search
```

If your Git version supports `switch`, this is preferred:

```bash
git switch master
git pull
git switch -c feature/add-user-search
```

## Commit Messages

Commit messages should explain what the commit does.

Use this format:

```text
<type>: <description>
```

Common types:

```text
feat:
fix:
refactor:
docs:
test:
chore:
build:
ci:
```

Examples:

```text
feat: add filtering to user search
fix: prevent duplicate form submission
refactor: move auth logic into service
docs: document local setup
test: add tests for expired sessions
chore: update eslint dependencies
```

### Commit message rules

Keep commits focused. A commit should ideally represent one logical change.

Avoid messages such as:

```text
update
changes
fix
wip
stuff
final
final final
```

## Committing Changes

Check what has changed:

```bash
git status
git diff
```

Stage the files you want:

```bash
git add path/to/file
```

Or stage everything intentionally:

```bash
git add .
```

Then commit:

```bash
git commit -m "feat: add user search"
```

Before committing, make sure you have not accidentally included:

* Secrets
* `.env` files
* Credentials
* Generated files that should be ignored
* Debugging code
* Unrelated changes

## Keeping Your Branch Up to Date

Before opening or updating a pull request, bring in the latest changes from the base branch.

```bash
git switch master
git pull
git switch feature/add-user-search
git rebase master
```

If the repository prefers merging instead of rebasing:

```bash
git switch feature/add-user-search
git merge master
```

Use the strategy defined by the repository. Do not mix merge and rebase workflows unnecessarily.

## Resolving Rebase Conflicts

If Git reports a conflict:

```bash
git status
```

Resolve the conflicting files, then stage them:

```bash
git add path/to/resolved-file
```

Continue:

```bash
git rebase --continue
```

To abandon the rebase:

```bash
git rebase --abort
```

## Pushing

Push a new branch with:

```bash
git push -u origin feature/add-user-search
```

After the upstream branch has been configured:

```bash
git push
```

If you rebased a branch that you have already pushed, use:

```bash
git push --force-with-lease
```

Do **not** use:

```bash
git push --force
```

unless there is a specific reason and you understand the consequences.

`--force-with-lease` is safer because it protects against accidentally overwriting someone else's remote changes.

## Pull Requests

Pull requests should have a clear title describing the change.

Preferred format:

```text
<type>: <description>
```

Examples:

```text
feat: add user search
fix: prevent duplicate checkout requests
refactor: simplify authentication middleware
```

A pull request should explain:

### What

What changed?

### Why

Why was the change necessary?

### Testing

How was the change tested?

Example:

```markdown
## What

Adds filtering by name and email to the user search page.

## Why

Support staff need a faster way to locate customer accounts.

## Testing

- Tested name search
- Tested email search
- Tested empty results
- Added unit tests for filtering
```

## Pull Request Size

Prefer small, focused pull requests.

Avoid combining unrelated work into the same PR.

If a change is becoming difficult to review, consider splitting it into multiple PRs.

For example:

```text
PR 1: database changes
PR 2: backend implementation
PR 3: frontend implementation
```

when those pieces can reasonably be reviewed independently.

## Reviewing Pull Requests

When reviewing code, focus on:

* Correctness
* Maintainability
* Readability
* Security
* Tests
* Edge cases
* Whether the change matches the intended behaviour

Distinguish between required changes and optional suggestions.

Useful prefixes include:

```text
blocking:
suggestion:
question:
nit:
```

Example:

```text
blocking: this can throw when the user is null

suggestion: this logic could be moved into the service

question: do we need to handle expired sessions here?

nit: this variable could have a more descriptive name
```

## Merging

Do not merge a pull request until:

* Required checks pass
* Required reviews are complete
* Requested changes have been addressed
* The branch is sufficiently up to date
* There are no unresolved conversations that block the change

Use the repository's configured merge strategy consistently.

## Deleting Branches

After a PR has been merged, delete the branch unless there is a reason to keep it.

Delete locally:

```bash
git branch -d feature/add-user-search
```

Delete remotely:

```bash
git push origin --delete feature/add-user-search
```

## Undoing Changes

Discard unstaged changes to a file:

```bash
git restore path/to/file
```

Unstage a file while keeping the changes:

```bash
git restore --staged path/to/file
```

Undo a commit by creating a new reversing commit:

```bash
git revert <commit>
```

Avoid rewriting shared Git history unless there is a clear reason to do so.

## Useful Commands

See the current state:

```bash
git status
```

See changes:

```bash
git diff
```

See staged changes:

```bash
git diff --staged
```

See recent commits:

```bash
git log --oneline
```

See local branches:

```bash
git branch
```

See local and remote branches:

```bash
git branch -a
```

Fetch remote changes without modifying your current branch:

```bash
git fetch
```

Temporarily store unfinished changes:

```bash
git stash
```

Restore them:

```bash
git stash pop
```

## General Rules

1. Do not commit directly to protected branches.
2. Do not commit secrets or credentials.
3. Keep branches focused on one piece of work.
4. Keep commits logical and understandable.
5. Pull or fetch regularly.
6. Review your diff before committing.
7. Review your own PR before requesting review.
8. Prefer `--force-with-lease` over `--force`.
9. Delete branches after they are merged.
10. When unsure about a destructive Git command, check what it will do before running it.

## Recommended Workflow

For most work:

```bash
# Get the latest master
git switch master
git pull

# Create your branch
git switch -c feature/add-user-search

# Make changes
git status
git diff

# Commit
git add .
git commit -m "feat: add user search"

# Update against master before submitting
git fetch origin
git rebase origin/master

# Push
git push -u origin feature/add-user-search
```

Then open a pull request and request review.

---

These conventions are intended to keep Git predictable rather than introduce unnecessary process. If a convention does not fit a particular repository, that repository's documented rules take precedence.
