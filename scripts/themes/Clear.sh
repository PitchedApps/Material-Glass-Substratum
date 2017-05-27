#!/usr/bin/env bash

themeXml() {
    echo "$(sed \
    -e 's/\$\$b\$/@android:drawable\/screen_background_dark_transparent/gi' \
    -e 's/\$t\$\([a-z0-9]\{2\}\)/#\1ffffff/gi' \
    -e 's/\$t\$/#fff/gi' \
    -e 's/\$tt\$/#ddffffff/gi' \
    -e 's/\$ttt\$/#88ffffff/gi' \
    -e 's/\$b\$\([a-z0-9]\{2\}\)/#\1000000/gi' \
    -e 's/\$b\$/#80000000/gi' \
    -e 's/\$bb\$/#80111111/gi' \
    -e 's/\$bbb\$/#80222222/gi' \
    -e 's/\$bt\$/#a0000000/gi' \
    -e 's/\$btt\$/#a0111111/gi' \
    -e 's/\$bttt\$/#a0222222/gi' \
    -e 's/\$c\$/#30000000/gi' \
    -e 's/\$o\$/#000/gi' \
    -e 's/\$oo\$/#111/gi' \
    -e 's/\$ooo\$/#222/gi' \
    -e 's/\$a\$/#748B96/gi' \
    -e 's/\$ab\$/#62757e/gi' \
    -e 's/\$at\$/#9db9c6/gi' \
    -e 's/\$r\$/#30ffffff/gi' \
    -e 's/\$rr\$/#10ffffff/gi' \
    -e 's/\$d\$/#d0060606/gi' \
    -e 's/\$dd\$/#a0000000/gi' \
    -e 's/\$ddd\$/#a0111111/gi' \
    -e 's/\$m\$/#888/gi' \
    -e 's/\$mm\$/#50888888/gi' \
    -e 's/\$wall\$/true/gi' \
    -e 's/\$base\$/@android:style\/Theme.Material.Wallpaper/gi' \
    <<< "$1")"
}