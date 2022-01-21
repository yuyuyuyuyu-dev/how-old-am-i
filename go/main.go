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
    year, err := strconv.Atoi(args[0].String())
    if err != nil {
        return ""
    }
    month, err := strconv.Atoi(args[1].String())
    if err != nil {
        return ""
    }
    day, err := strconv.Atoi(args[2].String())
    if err != nil {
        return ""
    }

    dateOfBirth, err := time.Parse("2006/01/02", fmt.Sprintf("%04d", year) + "/" + fmt.Sprintf("%02d", month) + "/" + fmt.Sprintf("%02d", day))
    if err != nil {
        return ""
    }

    return "あなたは " + strconv.Itoa(calcAge(dateOfBirth)) + "歳 です"
}


func calcAge(dateOfBirth time.Time) int {
    now := time.Now()
    splitNow := strings.Split(now.Format("2006/01/02"), "/")

    splitDateOfBirth := strings.Split(dateOfBirth.Format("2006/01/02"), "/")

    nowYear, _ := strconv.Atoi(splitNow[0])
    birthYear, _ := strconv.Atoi(splitDateOfBirth[0])
    age := nowYear - birthYear

    currentBirthDay, _ := time.Parse("2006/01/02", splitNow[0] + "/" + splitDateOfBirth[1] + "/" + splitDateOfBirth[2])
    if  currentBirthDay.After(now) {
        age -= 1
    }

    return age
}
