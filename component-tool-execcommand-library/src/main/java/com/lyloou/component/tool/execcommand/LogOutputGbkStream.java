/*
 * Copyright (C) 2014 ZeroTurnaround <support@zeroturnaround.com>
 * Contains fragments of code from Apache Commons Exec, rights owned
 * by Apache Software Foundation (ASF).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lyloou.component.tool.execcommand;

import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Base class to connect a logging system to the output and/or
 * error stream of then external process. The implementation
 * parses the incoming data to construct a line and passes
 * the complete line to an user-defined implementation.
 */
public class LogOutputGbkStream extends LogOutputBufferStream {
    private final ExecTask task;
    private final ExecCommand execCommand;

    public LogOutputGbkStream(ExecTask task, ExecCommand execCommand) {
        this.task = task;
        this.execCommand = execCommand;
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    @Override
    protected void processLine(ByteArrayOutputStream buffer) {
        String s = buffer.toString("GBK");
        task.setProcessLine(s);
        execCommand.notifyTaskChanged();
    }
}
