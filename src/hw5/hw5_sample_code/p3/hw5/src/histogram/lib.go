package histogram

import (
	"math/rand"
	"time"
)

type Hist struct {
	Bins       []int
	NumSamples int
}

func makeRand() *rand.Rand {
	return rand.New(rand.NewSource(time.Now().UTC().UnixNano()))
}

func New(upperBound int) Hist {
	return Hist{
		Bins:       make([]int, upperBound),
		NumSamples: 0,
	}
}

func (h *Hist) merge(other Hist) (err bool) {
		hist := *h
		if hist.NumSamples != other.NumSamples {
			return false
		}
		hist.Bins = append(h.Bins, other.Bins...)
		hist.NumSamples =h.NumSamples + other.NumSamples
		*h = hist
		return true
}

func Make(numSamples, upperBound int) Hist {
		arr := make([]int, upperBound)
    histogram := new Hist(numSamples)
		for i := 0; i < numSamples; i++ {
			histogram.Bins[rand.Intn(upperBound)]++
		}
		return histogram
}

// Makes a histogram using the indicated number of workers running in parallel.
func MakeInParallel(numSamples, upperBound, numWorkers int) Hist {
		ch := make(chan Hist)
		rangePerWorker := upperBound / numWorkers
		samplesPerWorker := numSamples / numWorkers
    for i := 0; i < numWorkers; i++ {
			go Worker(rangePerWorker, samplesPerWorker, ch)
		}
		w, x := <-ch, <-ch
		w.merge(x)
		y, z := <- ch, <- ch
		y.merge(z)
		w.merge(y)
		return w
}

func Worker(rangePerWorker int, samplesPerWorker int, channel chan Hist) {
	arr := make([]int, rangePerWorker)
	for i := 0; i < samplesPerWorker; i++ {
		arr[rand.Intn(rangePerWorker)]++
	}
	channel <- Hist{arr, samplesPerWorker}
}
