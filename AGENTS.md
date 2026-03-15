# AGENTS — How to generate command documentation

This document describes a simple, repeatable approach for generating or
updating `docs/commands/CommandXX.md` files using an automated agent or
script. The goal is to keep command docs consistent and machine-friendly.

Template for a command doc

Include these sections (in order):

- Title: `Command ID XX — Short name`
- Short description: one-paragraph summary of the command's purpose.
- Summary: bullet list with `Direction`, `Purpose`, `Key fields`.
- Frame structure (example): single example table showing ID, LEN, payload,
  CRC.
- Fields: bullet descriptions for named bytes/fields (X, Y, height, width, etc.).
- Parsing / rendering notes: any decoding rules or gotchas (endianness,
  bit-order, color semantics).
- Notes & tips: operational advice for correlating frames or known quirks.
- See also: links to related docs and parsing implementations.

Suggested automated workflow

1. Collect examples: parse captured logs (CSV, PCAP, or repo `example/data.csv`) and
   extract frames grouped by command ID.
2. Infer canonical field names by applying heuristics (e.g. common byte positions
   for X/Y/width/height; two-byte values -> endianness check).
3. Generate a markdown file using the template above and the inferred values.
4. Add a short human review pass to verify heuristics (endianness, color meanings).

Tooling hints

- A small Node.js or Python script is sufficient. Steps: read CSV, group by ID,
  sample representative frames, and render the template.
- If you use an LLM agent, provide it with a single representative frame plus
  parsing code (e.g. `commands/Command31.kt`) so it can infer correct bit/byte
  semantics.
- Keep generated files under `docs/commands/` and run a lint check (markdown
  preview) as part of CI.

Example CLI (pseudo-steps)

1. `node scripts/generate-command-docs.js --source example/data.csv --out docs/commands`
2. Review and commit: `git add docs/commands/* && git commit -m "docs: regen command docs"`

If you'd like, I can create a starter script (`scripts/generate-command-docs.js`)
that parses `example/data.csv` and emits command markdown files using this
template. Tell me whether you prefer Node.js or Python.

Repository command implementations and docs

- The repository already contains implementations and docs for several commands.
- Source implementations: `example/src/Command04.ts`, `example/src/Command0A.ts`,
  `example/src/Command0D.ts`, `example/src/Command31.ts`, `example/src/Command39.ts`,
  and `example/src/Command55.ts`.
- Generated/handwritten docs: `docs/commands/Command04.md`, `docs/commands/Command0A.md`,
  `docs/commands/Command0D.md`, `docs/commands/Command31.md`, `docs/commands/Command39.md`,
  and `docs/commands/Command55.md`.

Suggested small workflow tuned to this repo

1. Run the collector against `example/data.csv` to gather frames by command ID.
2. For each observed ID, prefer reusing the existing parser in `example/src/CommandXX.ts`.
3. Emit a `docs/commands/CommandXX.md` using the template above and include a
   reference to the corresponding implementation file.
4. Manually review generated docs for field names, endianness, and semantics.

If you want, I can scaffold the generator script (Node.js or Python) and wire it
to produce files in `docs/commands/` using the existing `example/src/` parsers
as examples.
