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
