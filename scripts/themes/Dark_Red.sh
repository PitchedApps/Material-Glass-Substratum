#!/usr/bin/env bash

themeXml() {
    echo "$(sed \
    -e 's/\$\$b\$/@android:drawable\/screen_background_dark/gi' \
    -e 's/\$t\$\([a-z0-9]\{2\}\)/#\1F44336/gi' \
    -e 's/\$t\$/#F44336/gi' \
    -e 's/\$tt\$/#ddF44336/gi' \
    -e 's/\$ttt\$/#88F44336/gi' \
    -e 's/\$b\$\([a-z0-9]\{2\}\)/#\1000000/gi' \
    -e 's/\$b\$/#000/gi' \
    -e 's/\$bb\$/#111/gi' \
    -e 's/\$bbb\$/#222/gi' \
    -e 's/\$bt\$/#a0000000/gi' \
    -e 's/\$btt\$/#a0111111/gi' \
    -e 's/\$bttt\$/#a0222222/gi' \
    -e 's/\$c\$/#111/gi' \
    -e 's/\$o\$/#000/gi' \
    -e 's/\$oo\$/#111/gi' \
    -e 's/\$ooo\$/#222/gi' \
    -e 's/\$a\$/#2196F3/gi' \
    -e 's/\$ab\$/#222/gi' \
    -e 's/\$at\$/#64B5F6/gi' \
    -e 's/\$r\$/#30F44336/gi' \
    -e 's/\$rr\$/#10F44336/gi' \
    -e 's/\$d\$/#060606/gi' \
    -e 's/\$dd\$/#000/gi' \
    -e 's/\$ddd\$/#111/gi' \
    -e 's/\$m\$/#888/gi' \
    -e 's/\$mm\$/#50888888/gi' \
    -e 's/\$wall\$/false/gi' \
    -e 's/\$base\$/@android:style\/Theme.Material/gi' \
    <<< "$1")"
}

mainColor() {
    echo "#F44336"
}