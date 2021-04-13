#!/bin/bash
#
# @author Timotej Ponek xponek00
# @author Timotej Kamensky xkamen24
# @copyright Brno university of technology, faculty of computer science, Czechia.
# @brief assignment of java application for basic warehouse management system

wget -O javafx.zip https://gluonhq.com/download/javafx-11-0-2-sdk-linux/
unzip javafx.zip
rm javafx.zip
mkdir lib
mv -v javafx-sdk-11.0.2/lib/* ../lib/lib/
rm -rf javafx-sdk-11.0.2