# Command ID Loading Sequence — `to` commands and ACKs

Short description: Implementation notes for the initial loading sequence observed in `example/data.csv` (first frames). This document covers the `to` frames in the startup/load handshake and the expected ACK frames (IDs `0x01`, `0x03`, `0x09`).

Summary:

- **Direction:** `to` = host -> device (send)
- **Purpose:** Boot/load handshake during startup
- **Key fields:** `ID`, `LEN`, `PLC`, `DATA`, `CRC`

Reference trace: see [example/data.csv](example/data.csv#L1-L6) for the raw captured frames.

**Loading Steps**

- **Step A — Command `0x00` (host -> device)**
  - **Frame (observed):** ID=`0x00`, LEN=`0x02`, PLC=`0xFD`, DATA=`0x00`, CRC=`0xFF` (see trace).
  - **Expected ACK:** ID=`0x01`, LEN=`0x02`, PLC=`0xFD`, DATA=`0x00`, CRC=`0xFE`.
  - **Implementation notes:** send the full frame; wait for a `from` frame with ID `0x01`. Verify the ACK's DATA byte equals `0x00` (matches step) and validate CRC if you compute it. If no ACK within timeout, retry once or follow device retry policy.

- **Step B — Command `0x02` (host -> device)**
  - **Frame (observed):** ID=`0x02`, LEN=`0x04`, PLC=`0xFB`, DATA=`0x01 0x01 0x00`, CRC=`0xFD`.
  - **Expected ACK:** ID=`0x03`, LEN=`0x02`, PLC=`0xFD`, DATA=`0x01`, CRC=`0xFD`.
  - **Implementation notes:** after sending `0x02`, wait for `from` ID `0x03`. The ACK contains a single DATA byte `0x01` (confirmation/step id). Treat non-matching DATA or wrong ID as a failure.

- **Step C — Command `0x04` (host -> device)**
  - **Frame (observed):** ID=`0x04`, LEN=`0x0B`, PLC=`0xF4`, DATA=`0x02 0x53 0x10 0x31 0x30 0x00 0x50 0x00 0xE0 0x11`, CRC=`0x1A`.
  - **Expected ACK:** ID=`0x09`, LEN=`0x02`, PLC=`0xFD`, DATA=`0x02`, CRC=`0xF4`.
  - **Implementation notes:** this larger frame conveys configuration payload; expect ACK ID `0x09` with DATA `0x02`. Verify ACK ID and DATA match; validate CRC if implemented.

**General implementation guidance**

- Use the CSV trace as a canonical example: [example/data.csv](example/data.csv#L1-L6).
- ACK IDs for the three loading steps are: `0x01` (for `0x00`), `0x03` (for `0x02`), and `0x09` (for `0x04`).
- On send: build frames as (ID, LEN, PLC, DATA..., CRC). Ensure CRC algorithm matches device expectations (the trace includes CRC bytes).
- On receive: match ACK by frame ID first, then confirm ACK DATA content (single-byte confirmation in these examples). If the ACK ID arrives but DATA differs, treat as NACK/invalid and handle per your retry/error policy.

If you want, I can also:

- add examples of code to build/send frames and compute CRC, or
- generate per-command markdown files under `docs/commands/` linking to this loading sequence summary.
