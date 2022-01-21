package main

import (
	"fmt"
	"strconv"
	"strings"
	"time"
)


func main() {
    dateOfBirth := "2023/01/20"
    fmt.Println(howOldAmI(dateOfBirth))
}


func howOldAmI(dateOfBirth string) int {
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
