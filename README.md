# BerkeleyAlgorithm
Algorytm z Berkeley służący do synchronizowania zegarów różnych urządzeń. W prezentowanym przykładzie zegary klientów posiadają dryf. 

1. Serwer czasu (klient RMI) pobiera czas własny oraz czasy klientów (serwery RMI).
2. Liczy średnią czasów ignorując czasy zbyt odbiegające od jego własnego. Wartość "zbyt odbiegająca" została ustalona na 1 minutę.
3. Serwer czasu wysyła wszystkim klientom wartość o jaką muszą przestawić swoje zegary, by dorównać do średniej. Swój czas również dostosowuje do średniej. Klient zamiast cofać swój czas będzie ten czas przeczekiwał. Przesyłane wartości uwzględniają czas odpowiedzi ustalony na 10ms.
4. Co 5 sekund proces się powtarza.  
