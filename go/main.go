package main

import (
	"fmt"
	"strconv"
	"strings"
	"syscall/js"
	"time"
)


func main() {
    c := make(chan struct{}, 0)
    registerCallbacks()
    <-c
}


func registerCallbacks() {
    js.Global().Set("howOldAmI", js.FuncOf(howOldAmI))
}


func howOldAmI(this js.Value, args []js.Value) interface{} {
    fmt.Println(args)
    return "hoge"
}


func calcAge(dateOfBirth string) int {
    now := time.Now()
    splitNow := strings.Split(now.Format("2006/01/02"), "/")

    splitDateOfBirth := strings.Split(dateOfBirth, "/")

    nowYear, _ := strconv.Atoi(splitNow[0])
    birthYear, _ := strconv.Atoi(splitDateOfBirth[0])
    age := nowYear - birthYear

    currentBirthDay, _ := time.Parse("2006/01/02", splitNow[0] + "/" + splitDateOfBirth[1] + "/" + splitDateOfBirth[2])
    if  currentBirthDay.After(now) {
        age -= 1
    }

    return age
}
