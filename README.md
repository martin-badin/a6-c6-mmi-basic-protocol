# MMI Basic Protocol — Monochrome Display (Audi A6 C6 / 4F)

![Display preview](resources/load_screen.jpg)
![Display preview](resources/image.jpg)

This repository collects captures, parsers and partial decoders for the MMI
Basic monochrome display protocol used in some Audi A6 C6 (4F) vehicles. The
primary goals are to document the protocol, decode command structures and
provide tools to emulate or replace the OEM display (for research and
educational purposes).

## Overview

- Communication path: CAN (head unit) → CAN-to-UART converter (display) →
  LCD controller
- Observed display resolution: 224 × 80 px (see `commands/Command04.kt`)

## What’s in this repository

- `CanFrame.kt` — CAN frame parsing and helpers
- `CommandModel.kt` — common command model types and validation
- `DisplayView.kt`, `loadScreen.kt` — display helpers / static screens
- `data.csv` — captured or annotated data excerpts
- `loadScreen.csv` — load screen data with test Audi Multi Media Interface
- `commands/` — individual command parsers/decoders (e.g. `Command31.kt`)
- `docs/` — documentation (command reference, CAN frame, PLC, etc.)
- `LICENSE` — project license

## Quick summary

- Protocol: CAN → CAN-to-UART → LCD controller (monochrome)
- Key commands:
  - `0x04` — resolution / display info
  - `0x31` — screen data (pixel blocks)
  - `0x39` — clear/area fill
  - `0x55` — brightness

## Debugging / Hardware

I use a simple UART→USB converter to tap the CAN-to-UART output from the
display module for logging and analysis. Converter settings and helper code
are implemented in `USBDevice.kt`.

The CAN-to-UART converter IC used in the logging setup is the A82C250.
This transceiver bridges the vehicle CAN bus and the UART-side converter
hardware used to capture frames.

## Documentation

- Command reference: `docs/commands.md` (index linking to per-command files)
- CAN frame fields and PLC: `docs/canframe.md`

## Contribution

Contributions are welcome. Good first tasks:

- Add decoding for unclear command fields
- Improve documentation for command structures and examples
- Add a small visualizer that renders frames to an image
- Create an Android app that renders display data directly in an Android
  head unit
- Integrate the app into an Android head unit (replace/extend the
  existing display app)

Please open issues for questions or submit pull requests for code changes.

## License

See [LICENSE](LICENSE) for license terms.

---

Updated README — key files: [docs](docs) and [LICENSE](LICENSE)
