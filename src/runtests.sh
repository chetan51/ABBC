#!/bin/bash

javac -d build @java.list
cd build
java junit.textui.TestRunner test.java.Client.TestMessageController
