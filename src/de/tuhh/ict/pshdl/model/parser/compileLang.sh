#!/bin/bash
cd /Users/karstenbecker/Dropbox/PSHDL/de.tuhh.ict.pshdl.model.v2/src/de/tuhh/ict/pshdl/model/parser
antlr4 -package de.tuhh.ict.pshdl.model.parser -no-listener PSHDLLang.g4
