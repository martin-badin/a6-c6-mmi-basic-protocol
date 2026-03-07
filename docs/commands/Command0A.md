```markdown
# Command ID 0A — Host request / block request

This file documents the `0x0A` frames sent by the host to request a
display block from the device.

Summary

- Direction: host -> device
- Purpose: request transfer of a display block identified by a block id
- Key fields: block id (payload[0]), flags/params

Frame structure (observed examples):

| ID   | LEN  | PLC  | Data (example)           | CRC  |
| ---- | ---- | ---- | ------------------------ | ---- |
| 0x0A | 0x06 | 0xF9 | 0x03 0x01 0x03 0xDE 0x02 | 0x28 |
| 0x0A | 0x06 | 0xF9 | 0x05 0x02 0x00 0x16 0x04 | 0xE0 |
| 0x0A | 0x06 | 0xF9 | 0x07 0x02 0x00 0x0F 0x05 | 0xFA |

Notes & tips

- The first byte of the payload is consistently the requested block id
  (examples: `0x03`, `0x05`, `0x07`, `0x0A`).
- The remaining bytes vary and appear to include flags or small parameters;
  their exact meaning is not fully identified.

Recommended parsing approach

- Treat `0x0A` as a request message: extract `blockId = data[0]` and store
  any immediately-following flags/params for later correlation.
- When correlating logs, use the sequence `0x0A (request N)` -> `0x0D (ACK N)` ->
  `0x31`/`0x39` (content for N).

See also

- `docs/commands/Command0D.md` — device ACKs for requested blocks
- `docs/commands/Command31.md` — pixel block payloads

If desired, I can add a small parser helper (`Command0A.kt` / JS) that
extracts named fields and attaches the request to subsequent device frames.
```
