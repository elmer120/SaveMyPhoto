<a href="https://github.com/elmer120/SaveMyPhoto/blob/master/TesinaProgetto.pdf">Tesina esame di stato 2016 in PDF</a>


# Tesina esame di stato 2016/ 

# MEDIA IN CLOUD

## A cura di Marco Pedrazzi

## – Classe 5si –

## Istituto Tecnico Tecnologico Marconi Rovereto

## Indirizzo informatico


## Sommario

- Tesina esame di stato 2016/2017
- Sigle e abbreviazioni
- Introduzione
   - Tecnologie utilizzate
   - Panoramica ambiente Android......................................................................................................................
   - Funzionalità
      - Scelta accesso
      - Registrazione
      - Accesso
      - Galleria
      - Presentazione
      - Sincronizzazione
      - Parte Web
- Sfide affrontate
      - Content Provider
      - Adapter
      - AsyncTask
      - Http
      - Conclusioni
      - Sitografia e fonti


## Sigle e abbreviazioni

View= è una vista che mostra un componente grafico e gestisce eventi relativi all’interazione con l’utente.

CAB= Contextual Action Bar può essere considerata una Action Bar temporanea che riporta i comandi

attivabili sotto forma di _action._

GridView = è una View di cui dispone Android, consente di disporre in modo dinamico gli elementi in

formato a griglia.

ImageView = è una View, che consente di visualizzare immagini ed interagire con esse.


## Introduzione

Mentre stavo pensando a un’idea per il progetto di fine anno, ho notato che uno dei dispositivi che ha

certamente rivoluzionato maggiormente le nostre abitudini è lo smartphone.

Esso ha ormai tutte le caratteristiche di un pc, ed è completo di tante funzionalità aggiuntive, come sensori

di prossimità, GPS, giroscopio ed altro, ma una delle più utilizzate resta certamente la fotocamera.

Ormai si scattano centinaia di fotografie con estrema facilità, occupando velocemente la memoria del

dispositivo. Per risolvere questo problema ci sono molte app, la più diffusa e conosciuta è Google photo. Nel

mio progetto ho pensato di svilupparne una semplice versione.

### Tecnologie utilizzate

L’applicativo utilizza le seguenti tecnologie:

**App Parte web**

Linguaggio: Java Linguaggi: C#, Html,Css

```
Framework Android
(classi che estendono java)
```
```
ADO.NET Entity Framework
(mappa il database in classi)
```
```
SqlLite
(database)
```
```
SQL
(database)
```
Libreria Soap

(interfaccia/connessione al Web Service)

Web Service Soap

```
(Consente di invocare servizi da parte di
applicazioni esterne)
```
- Bootstrap
    (Grafica e Scripting)

### Panoramica ambiente Android......................................................................................................................

Android è un sistema operativo per

dispositivi mobili, è stato sviluppato da

Google, partendo da una base open

source, il kernel Linux.

Le app in assenza di ulteriori

framework (Apache Cordova,

AppInventor, etc) vengono sviluppate

in java che attinge da un framework

standard integrato nel sistema che

estende le librerie classiche di java.

Ogni app viene eseguita tramite un

compilatore ibrido, la Dalvik virtual

Machine o la più recente Android Run

Time.


Da notare che in android tutto è un’app: la schermata home, il menu e persino l’app che effettua le chiamate

telefoniche.

Con la sua flessibilità e leggerezza riesce ad adattarsi a molte categorie di dispositivi, smartphone, orologi da

polso, Smart-tv, occhiali, eccetera. Ad oggi è il sistema operativo più diffuso su dispositivi mobili, con una

quota di mercato pari al 62%.

Un’arma a doppio taglio secondo me è il rapido rilascio di nuove versioni, che se da un lato introduce sempre

notevoli migliorie e stabilità al sistema, dall’altra richiede un notevole impegno sia ai produttori di

smartphone che agli sviluppatori di app per mantenere il software aggiornato e compatibile con le nuove

release.

Entrando più in dettaglio, un’app è composta da diverse schermate chiamate “activity”, che si susseguono in

modalità a stack; ogni activity ha il suo ciclo di vita che a livello di codice viene gestito tramite alcune callback.

A seguire qui un’immagine esplicativa.


Un’activity a sua volta, ma solo facoltativamente, può essere divisa in fragment: questo permette di

migliorare l’esperienza utente, permettendo a dispositivi come i tablet di disporre il contenuto in maniera

ottimale.

### Funzionalità

L’applicazione offre all’utente la possibilità di sincronizzare i propri media in cloud, permettendone la

visualizzazione sia su tutti i dispositivi associati all’account dell’utente, sia sull’applicazione web. In entrambi

i casi, per l’accesso vengono utilizzate le medesime credenziali.

Inoltre l’applicazione fornisce la possibilità di eliminare i media dal dispositivo, mantenendo comunque la

possibilità di visualizzazione grazie al download run-time implementato; questo consente di avere più spazio

libero sul dispositivo.


#### Scelta accesso

La prima activity dell’app offre la possibilità di scegliere se creare un nuovo account oppure accedere con

un account già esistente.

```
1 Activity iniziale.
```
#### Registrazione

Permette all’utente la creazione di un account, scegliendo nome utente e password.

La registrazione necessita di una connessione attiva per contattare il server, che attraverso il web service

inserisce il record nel database e, oltre alle informazioni richieste, registra anche informazioni sul dispositivo.

_2 Activity registrazione 2.1 screenshot tabella utenti e dispositivi._


#### Accesso

Se l’accesso viene effettuato con un account già esistente, il nuovo dispositivo verrà associato a

quell’account; questa modalità necessita di una connessione attiva.

Altrimenti l’accesso può essere eseguito in modalità offline, in quanto le credenziali sono memorizzate sul

database del dispositivo; questo è stato previsto per consentire la visualizzazione dei media locali al

dispositivo.

_3 Activity accedi 3.1 Screenshot tabelle utenti e dispositivi._

#### Galleria

Dopo l’accesso vengono visualizzati:

I media che sono presenti sul dispositivo.

Media sincronizzati da altri dispositivi associati all’account.

Media sincronizzati dello stesso dispositivo.

I media sono visualizzati, nel componente Gridview, con ordine decrescente in base alla data di

acquisizione.


#### Presentazione

Esiste la possibilità di visualizzare i media a pieno schermo, navigando tra gli stessi con un “swipe”.

#### Sincronizzazione

Attivando la CAB con un touch lungo sul media, si può eseguire una selezione multipla.

Per effettuare l’upload, sarà sufficiente aprire il menu contestuale e cliccare “backup su server”.

Per quanto riguarda il download dei media già sincronizzati, viene effettuato automaticamente.

_4 Action mode Upload terminato Altro dispositivo associato all’account_


#### Parte Web

È possibile accedere tramite la pagina di

Login con le credenziali utilizzate sui

dispositivi.

Nella pagina seguente si vedranno tutte

le foto sincronizzate, oppure suddivise

per dispositivo.

E’ possibile visualizzare a schermo intero, tramite lo slideshow implementato.


## Sfide affrontate

#### Content Provider

Uno dei primi scogli che ho incontrato, è come recuperare i media dal dispositivo. Inizialmente ho cercato di

fare una “scan” del filesystem, ma ho visto che era molto “macchinoso” ed inoltre c’è una mancanza di

standard nei nomi assegnati alle cartelle.

L’app fotocamera infatti memorizza le foto sempre nella cartella “DCMI” che di fatto è uno standard, ma i

vari brand utilizzano nomi diversi per le sottocartelle, oppure non fanno uso di sub-directory.

La soluzione che ho trovato dopo varie ricerche, è utilizzare

un content provider; questo componente di

android altro non è che un database in sqllite

condiviso tra tutte le app d’ambiente.

Esso è suddiviso in settori: sotto una tabella

esplicativa.


Io ho utilizzato il MediaStore, che indicizza video, immagini, musica ed è interrogabile nel seguente modo:

**private** Cursor searchImage(String where){
_//istanzio il content resolver neccessario per interrogare il content provider (mediaStore)_
ContentResolver contentResolver = **this**. **ctx** .getContentResolver();

_//definisco le colonne che voglio estrarre_
**final** String[] selezione = {
MediaStore.Images.Media. **_DATA_** ,
MediaStore.Images.Media. **_DATE_TAKEN_** ,
MediaStore.Images.Media. **_DISPLAY_NAME_** ,
MediaStore.Images.Media. **_BUCKET_DISPLAY_NAME_** ,
MediaStore.Images.Media. **_MIME_TYPE_** ,
MediaStore.Images.Media. **_SIZE_** ,
MediaStore.Images.Media. **_HEIGHT_** ,
MediaStore.Images.Media. **_WIDTH_** ,
MediaStore.Images.Media. **_ORIENTATION_** ,
MediaStore.Images.Media. **_LATITUDE_** ,
MediaStore.Images.Media. **_LONGITUDE_** };

_//definisco l'ordine del interrogazione_
**final** String orderBy = MediaStore.Images.Media. **_BUCKET_DISPLAY_NAME_** + **" and
"** +MediaStore.Images.Media. **_DATE_TAKEN_** + **" DESC"** ;

_//eseguo query sul content provider_
Cursor cursor = contentResolver.query(
MediaStore.Images.Media. **_EXTERNAL_CONTENT_URI_** , _//URI risorsa da interrogare_
selezione, _//colonne da estrarre_
where, _//(WHERE in SQL)_
**null** ,
orderBy); _//selezione (ORDER BY in SQL)_

_//esempio query generata
//SELECT _data, datetaken, bucket_display_name, mime_type
// FROM images WHERE (bucket_display_name='100MEDIA')
// ORDER BY bucket_display_name and datetaken DESC_
**return** cursor;
}

Quando ottengo l’oggetto cursor, ciclo le informazioni che ritorna, creo l’oggetto FileMedia e lo aggiungo

alla lista.

**public** ArrayList<FileMedia> getListMedia(Album album)
{
**this**. **listMedia** = **null** ;
**this**. **listMedia** = **new** ArrayList<FileMedia>();
FileMedia fileMedia= **null** ;
String where= **null** ;

_//controllo che album si vuole estrarre_
**switch** (album)
{
**case** **_Camera_** :
where= **this**. **camera** ;
**break** ;
**case** **_WhatsApp_** :
where= **this**. **whatsApp** ;
**break** ;
**case** **_All_** :
where= **this**. **all** ;
**break** ;
}

_//recupero il cursore per le immagini_
Cursor cursorImage=searchImage(where);

_//recupero indice colonne_
**int** pathColumnIndex = cursorImage.getColumnIndex(MediaStore.Images.Media. **_DATA_** );
**int** dateColumnIndex = cursorImage.getColumnIndex(MediaStore.Images.Media. **_DATE_TAKEN_** );


**int** nameColumnIndex = cursorImage.getColumnIndex(MediaStore.Images.Media. **_DISPLAY_NAME_** );
**int** dirColumnIndex =cursorImage.getColumnIndex(MediaStore.Images.Media. **_BUCKET_DISPLAY_NAME_** );
**int** mimeColumnIndex = cursorImage.getColumnIndex(MediaStore.Images.Media. **_MIME_TYPE_** );
**int** sizeColumnIndex = cursorImage.getColumnIndex(MediaStore.Images.Media. **_SIZE_** );
**int** heightColumnIndex = cursorImage.getColumnIndex(MediaStore.Images.Media. **_HEIGHT_** );
**int** widthColumnIndex = cursorImage.getColumnIndex(MediaStore.Images.Media. **_WIDTH_** );
**int** orientationColumnIndex =cursorImage.getColumnIndex(MediaStore.Images.Media. **_ORIENTATION_** );
**int** latitudeColumnIndex = cursorImage.getColumnIndex(MediaStore.Images.Media. **_LATITUDE_** );
**int** longitudeColumnIndex = cursorImage.getColumnIndex(MediaStore.Images.Media. **_LONGITUDE_** );

_//recupero il numero di righe ritornate_
**int** countRow = cursorImage.getCount();

_//ciclo per ogni riga_
**for** ( **int** i = 0 ; i < countRow; i++)
{
fileMedia= **null** ;
_//sposto il cursore sulla ennesima riga_
cursorImage.moveToPosition(i);
String path = cursorImage.getString(pathColumnIndex);
File file= **new** File(path);

_//se il file esiste_
**if** (file.exists())
{
String bucket = cursorImage.getString(dirColumnIndex);
String nome = cursorImage.getString(nameColumnIndex);
String mimeType= cursorImage.getString(mimeColumnIndex);
Integer dimensione = cursorImage.getInt(sizeColumnIndex);
Integer altezza = cursorImage.getInt(heightColumnIndex);
Integer larghezza = cursorImage.getInt(widthColumnIndex);
String orientamento= cursorImage.getString(orientationColumnIndex);
Double latitudine= cursorImage.getDouble(latitudeColumnIndex);
Double longitudine= cursorImage.getDouble(longitudeColumnIndex);

Long timestamp = cursorImage.getLong(dateColumnIndex);
Date dataAquisizione= **new** Date();
dataAquisizione.setTime(timestamp);

Boolean suServer= **false** ;
Boolean suDispositivo= **true** ;

_//se il media è già presente nel db locale_
**if** ( **dBgestione** .CheckMedia(nome))
{
_//significa che è già sul server_
suServer= **true** ;
}

_//creo ed aggiungo il media alla lista_
fileMedia = **new** FileMedia(dataAquisizione,path,nome,bucket,
mimeType,dimensione,altezza,larghezza,orientamento,
latitudine,longitudine,suServer,suDispositivo);

**this**. **listMedia** .add(fileMedia);

}
}
_//chiudo il cursore_
cursorImage.close();

Collections. _sort_ ( **listMedia** );
**return this**. **listMedia** ;
}


#### Adapter

Popolata la lista di media, devo mostrarla nel componente gridView: per fare questo sono necessari alcuni

step.

```
Questi step vengono eseguiti per ogni FileMedia
```
_Attributi del oggetto FileMedia_

Qui il metodo della classe ImageAdapter che genera le views; nel mio caso carico nel imageView il bitmap in maniera

asincrona grazie alla classe AsyncTask che descriverò nel paragrafo successivo.

_// crea un imageview per ogni view della gridview_
**public** View getView( **int** position, View convertView, ViewGroup parent)
{

FileMedia media= **listMedia** .get(position);
ImageViewOverlay imageViewOverlay;
_//controllo se esiste già la view e posso riutilizzarla_
**if** (convertView != **null** )
{
imageViewOverlay = (ImageViewOverlay) convertView;
_//reimposto gli attributi corretti_
imageViewOverlay. **suServer** =media.getSuServer();
imageViewOverlay. **suDipositivo** =media.getSuDispositivo();
}
**else**
{
imageViewOverlay = **new** ImageViewOverlay( **ctx** ,media.getSuServer(),media.getSuDispositivo());


imageViewOverlay.setLayoutParams( **new** GridView.LayoutParams( 250 , 250 ));
}

Bitmap bitmap= **null** ;

bitmap = **cachePhoto** .get(media.getDataAcquisizione().getTime());

**if** (bitmap != **null** )
{
imageViewOverlay.setImageBitmap( **cachePhoto** .get(media.getDataAcquisizione().getTime()));
Log. _d_ ( **"Load image: "** , **"Cache"** );
}
**else** _//se non presente in cache la carico in modo asincrono_
{
**try**
{

CaricaImmagine( **this**. **ctx** ,media,imageViewOverlay,position);  **Carico in maniera asincrona**
Log. _d_ ( **"Load image: "** , **"Async"** );

}
**catch** (Exception e)
{
Log. _d_ ( **this** .getClass().getSimpleName(),e.getMessage());
}
}

_//l'immagine è selezionata?_
**if** (media.getSelezionata())
{
**return** SelezionaImmagine(imageViewOverlay, **true** );
}
**else**
{
**return** SelezionaImmagine(imageViewOverlay, **false** );
}

##### }

#### AsyncTask

Come visto nel capitolo precedente, ogni volta che l’adapter genera un ImageView carico all’interno dello stesso un

oggetto Bitmap.

Fare questo direttamente nella classe ImageAdapter non è possibile, perché impegna troppo il thread principale, che

deve occuparsi anche di tenere la grafica fluida ed aggiornata. Per questo carico i media in maniera asincrona

estendendo la classe AsyncTask.

I metodi principali di AsyncTask:

1. onPreExecute: viene eseguito sul thread principale, contiene il codice di inizializzazione dell’interfaccia grafica.
2. doInBackground: eseguito in un thread in background si occupa di eseguire il task vero e proprio.
3. onPostExecute: eseguito nel thread principale e si occupa di aggiornare l’interfaccia dopo l’esecuzione per

mostrare i dati scaricati.

La classe _AsyncTask_ definisce tre parametri in ingresso:

1° parametro: input di doInBackground()

2° parametro: On progress update (opzionale)

3° parametro: output di doInBackground()


_//OnPreExecute e OnPostExecute vengono eseguiti sul thread principale_

_//1° parametro input doInBackgroud -- 3°parametro output doInBackground_
**public class** LoadPhotoBackground **extends** AsyncTask<FileMedia,Void,Bitmap> {
**private int larghezzaMaxMedia** = 100 ;
**private int altezzaMaxMedia** = 100 ;
WeakReference<ImageViewOverlay> **imageViewReferences** ;
**private** MemoryCachePhoto **cachePhoto** ;
**private int posizione** ;
**private** Context **ctx** ;
FileMedia **media** ;

**public** LoadPhotoBackground(Context ctx, ImageViewOverlay imageViewOverlay, MemoryCachePhoto
cachePhoto, **int** posizione)
{
_//riferimento debole dell'imageView per l'adapter, serve per consentire al garbage collector
l'eliminazione_
**imageViewReferences** = **new** WeakReference<ImageViewOverlay>(imageViewOverlay);
**this**. **cachePhoto** =cachePhoto;
**this**. **posizione** =posizione;
**this**. **ctx** =ctx;
}
@Override
**protected void** onPreExecute()
{
**super** .onPreExecute();
Log. _w_ ( **"OnPreExecute"** , **""** );
}

_//metodo eseguito in un thread separato_
@Override
**protected** Bitmap doInBackground(FileMedia... params) {
**this**. **media** =params[ 0 ];
String mimeType = **this**. **media** .getMimeType();
String percorsoFile = **this**. **media** .getPath();
Bitmap anteprima = Bitmap. _createBitmap_ ( 200 , 200 , Bitmap.Config. **_RGB_565_** );
BitmapFactory.Options opzioniBitmap = **new** BitmapFactory.Options();
_//codifica più leggera ogni pixel 2 byte_
opzioniBitmap. **inPreferredConfig** = Bitmap.Config. **_RGB_565_** ;
String log= **""** ;

**try** {
**switch** (mimeType)
{
**case "video/mp4"** :
anteprima = ThumbnailUtils. _createVideoThumbnail_ (percorsoFile,
MediaStore.Video.Thumbnails. **_MICRO_KIND_** ); _//96x96 dp_
log+= **"Video"** ;
Log. _d_ ( **"Video: "** , **"H: "** + anteprima.getHeight() + **" W: "** + anteprima.getWidth()+ **" "** +log);
**break** ;
**case "image/jpg"** :
**case "image/jpeg"** :
**case "image/png"** :
_//cerco se esiste già un anteprima nei metadati exif_
ExifInterface exifInterface = **new** ExifInterface(percorsoFile);
Log. _i_ ( **this** .getClass().getSimpleName(),exifInterface.toString());
**if** (exifInterface.hasThumbnail())
{
**byte** [] thumbnail = exifInterface.getThumbnail();
opzioniBitmap. **inJustDecodeBounds** = **true** ;
anteprima = BitmapFactory. _decodeByteArray_ (thumbnail, 0 , thumbnail. **length** , opzioniBitmap);

_//Calcolo il corretto fattore di ridimensionamento_
opzioniBitmap. **inSampleSize** = calculateInSampleSize(opzioniBitmap);
_//decodifico_
opzioniBitmap. **inJustDecodeBounds** = **false** ;
anteprima = BitmapFactory. _decodeByteArray_ (thumbnail, 0 , thumbnail. **length** , opzioniBitmap);
log+= **"DatiExif"** ;

}
**else** { _//Altrimenti la creà scalando l'immagine_

opzioniBitmap. **inJustDecodeBounds** = **true** ;
_//estraggo solamente le dimensioni_
anteprima = BitmapFactory. _decodeFile_ (percorsoFile, opzioniBitmap);
_//Calcolo il corretto fattore di ridimensionamento_
opzioniBitmap. **inSampleSize** = calculateInSampleSize(opzioniBitmap);


opzioniBitmap. **inJustDecodeBounds** = **false** ;
_//ora decodifico_
anteprima = BitmapFactory. _decodeFile_ (percorsoFile, opzioniBitmap);
log+= **"NoDatiExif"** ;
}
**break** ;
**case "image/web"** :

Http http = **new** Http();
_// richiesta get_
**byte** [] arrayByte=http.Ricevi( **this**. **media** .getPath());
Log. _i_ ( **this** .getClass().getSimpleName(), **""** +arrayByte. **length** );

**if** (arrayByte!= **null** )
{
opzioniBitmap. **inJustDecodeBounds** = **true** ;
_//estraggo solamente le dimensioni_
anteprima = BitmapFactory. _decodeByteArray_ (arrayByte, 0 , arrayByte. **length** ,opzioniBitmap);
_//Calcolo il corretto fattore di ridimensionamento_
opzioniBitmap. **inSampleSize** = calculateInSampleSize(opzioniBitmap);
opzioniBitmap. **inJustDecodeBounds** = **false** ;
_//ora decodifico_
anteprima = BitmapFactory. _decodeByteArray_ (arrayByte, 0 , arrayByte. **length** ,opzioniBitmap);

}
**break** ;
}

} **catch** (IOException e)
{
e.printStackTrace();
}

**return** anteprima;
}

@Override
**protected void** onPostExecute(Bitmap bitmap) {

**cachePhoto** .put( **this**. **media** .getDataAcquisizione().getTime(),bitmap);
imageViewOverlay.setImageBitmap(bitmap);
}

_//calcola il fattore di scala ottimale per il media_
**private int** calculateInSampleSize(BitmapFactory.Options bmOptions)
{
Log. _d_ ( **"immagine originale: "** , **"H: "** + bmOptions. **outHeight** + **" W: "** + bmOptions. **outWidth** );
**final int** photoWidth = bmOptions. **outWidth** ;
**final int** photoHeight = bmOptions. **outHeight** ;
**int** scaleFactor = 1 ;
**if** (photoWidth > **larghezzaMaxMedia** || photoHeight > **altezzaMaxMedia** ) {
**final int** halfPhotoWidth = photoWidth / 2 ;
**final int** halfPhotoHeight = photoHeight / 2 ; **while** (halfPhotoWidth / scaleFactor >
**larghezzaMaxMedia** || halfPhotoHeight / scaleFactor > **altezzaMaxMedia** )
{
scaleFactor *= 2 ;
}
}
**return** scaleFactor;
}


#### Http

Come accennato nel capitolo **presentazione** sia nel upload che nel download dei media faccio uso del

protocollo http.

L’http ( _HyperText Transfer Protocol_ **)** è un protocollo di livello 7(applicazione) ed è basato sul testo; è usato come

sistema principale nella trasmissione delle informazioni sul web ed è basato su un’architettura Client-

Server.

È stateless, ed espone dei metodi per le richieste; i principali e più utilizzati sono:

_GET_

Metodo che utilizzo quando la classe LoadPhotoBackground incontra un media che è sul server e procede al

download.

**public byte** [] Ricevi(String linkFoto)
{
_//Creo l'oggetto url_
URL url = **null** ;
_//Istanzio la connessione/client_
HttpURLConnection client = **null** ;

**try**
{
url = **new** URL( **"http://savemyphoto.gear.host/"** +linkFoto);
_//istanzio il client_
client = (HttpURLConnection) url.openConnection();
_//imposto il metodo http da utilizzare_
client.setRequestMethod( **"GET"** );
_//user agent fake_
client.setRequestProperty( **"User-Agent"** , **"CodeJava Agent"** );
//imposto la codifica
client.setRequestProperty( **"charset"** , **"UTF-8"** );
_//non utilizzo il meccanismo di caching_
client.setUseCaches( **false** );
_//apro la connessione (da qui non posso più impostare nessuna proprietà di connessione)_
client.connect();
Log. _i_ ( **this** .getClass().getSimpleName(), **"Connessione aperta..."** );
}
**catch** (MalformedURLException e)
{
e.printStackTrace();
Log. _i_ ( **this** .getClass().getSimpleName(), **"Url nn corretto!"** );
} **catch** (IOException e)
{
e.printStackTrace();
Log. _i_ ( **this** .getClass().getSimpleName(), **"Problemi nel contattare il server!"** );
}

Quando leggo la risposta dal server il protocollo http risponde con dei codici “predefiniti” qui elenco i

principali:

**200 OK**. Il server ha fornito correttamente il contenuto nella sezione body.

**400 Bad Request.** La risorsa richiesta non è comprensibile al server.

**404 Not Found.** La risorsa richiesta non è stata trovata e non se ne conosce l'ubicazione. Di solito avviene

quando l'URI è stato indicato in modo incorretto, oppure è stato rimosso il contenuto dal server.


_//Leggo la risposta dal server_
**switch** (LetturaRispostaServerDownload(client))
{

**case** 200 :
{
Log. _i_ ( **this** .getClass().getSimpleName(), **"Download ok! Risposta server: 200 OK!"** );
**try**
{
InputStream inputStream=client.getInputStream();
ByteArrayOutputStream buffer = **new** ByteArrayOutputStream();
**int** nRead;
**byte** [] data = **new byte** [ 1024 ];
**while** ((nRead = inputStream.read(data, 0 , data. **length** )) != - 1 ) {
buffer.write(data, 0 , nRead);
}
client.disconnect();
**return** buffer.toByteArray();
}
**catch** (IOException e)
{
e.printStackTrace();
client.disconnect();
}

##### }

##### }

##### //DISCONNESSIONE

client.disconnect();

**return null** ;

_POST (multipart)_

Metodo che utilizzo quando faccio l’upload di uno o più media sul server cliccando “backup su server”.

**public boolean** Invia(String nomeUtente, Integer idDispositivo, ArrayList<FileMedia> listMedia)
{
_//Creo l'oggetto url_
URL url = **null** ;
**try**
{
url = **new** URL( **urlServer** );
}
**catch** (MalformedURLException e)
{
e.printStackTrace();
Log. _i_ ( **this** .getClass().getSimpleName(), **"Url nn corretto!"** );
}
_//Istanzio la connessione/client_
HttpURLConnection client = **null** ;
**try**
{
client = (HttpURLConnection) url.openConnection();
}
**catch** (IOException e)
{
e.printStackTrace();
Log. _i_ ( **this** .getClass().getSimpleName(), **"Problemi nel contattare il server!"** );
}
_//imposto che la connessione è in uscita(abilita la scrittura sul oggetto) e in entrata_
client.setDoOutput( **true** );
client.setDoInput( **true** );
client.setRequestProperty( **"charset"** , **"UTF-8"** );

_//non utilizzo il meccanismo di caching_
client.setUseCaches( **false** );


_//imposto il tipo di richiesta utilizzato_
**try**
{
client.setRequestMethod( **"POST"** );

}
**catch** (ProtocolException e)
{
e.printStackTrace();
Log. _i_ ( **this** .getClass().getSimpleName(), **"Metodo http impostato è errato!"** );
}

_//imposto che in caso di disconnessione ritenti_
client.setRequestProperty( **"Connection"** , **"Keep-Alive"** );

_//imposto il timeout della connessione ad infinito_
client.setConnectTimeout( 0 );

_//imposto il content type multipart + il delimitatore/separatore della form_
client.setRequestProperty( **"Content-Type"** , **"multipart/form-data; boundary="** + **separatore** );

_//user agent fake_
client.setRequestProperty( **"User-Agent"** , **"CodeJava Agent"** );

_//apro la connessione (da qui non posso più impostare nessuna proprietà di connessione)_
**try** {
client.connect();
Log. _i_ ( **this** .getClass().getSimpleName(), **"Connessione aperta..."** );

##### //STREAM

_//Faccio lo stream di quello che voglio inviare sulla connessione_

_//Comincio a scrivere sulla connessione_
DataOutputStream dos = **new** DataOutputStream(client.getOutputStream());

_//Parametri post standard_
dos.writeBytes(CreaMsgPost( **campoUno** , nomeUtente));
dos.writeBytes(CreaMsgPost( **campoDue** , idDispositivo.toString()));
_//fine parametri_

_//invio i media_
**for** (FileMedia media:listMedia)
{
Log. _i_ ( **this** .getClass().getSimpleName(), **"---\nUpload di "** +media.getNome()+ **"\n DimensioneOriginale:
"** +media.getDimensione());

dos.writeBytes(CreaHeadMsgMedia(media.getNome(), media.getMimeType()));

_//comprimo il media_
**byte** [] tmp=CreaBodyMsgMedia(media.getPath(), **qualitaJpeg** );
_//setto la nuova dimensione per inviarla al server in quanto la compressione fa perdere i metadati_
media.setDimensione(tmp. **length** );
dos.write(tmp);
dos.writeBytes(CreaTailMsgMedia());
}

_//chiudo la richiesta_
dos.writeBytes(FineRichiesta());
Log. _i_ ( **this** .getClass().getSimpleName(), **"Richiesta http conclusa."** );
} **catch** (IOException e) {
e.printStackTrace();
Log. _i_ ( **this** .getClass().getSimpleName(), **"Errore durante l'upload"** );
}

_//Leggo la risposta dal server_
**switch** (LetturaRispostaServer(client))
{

**case** 200 :
Log. _i_ ( **this** .getClass().getSimpleName(), **"Upload ok! Risposta server: 200 OK!"** );
_//aggiungo i record sul db remoto_
**if** (AggiungiMediaDbRemoto(listMedia,nomeUtente,idDispositivo))
{
_//aggiungo il media nel db locale_
**if** ( **this**. **dBgestione** .SyncListMedia(listMedia))
{
_//imposto che il media è sul server_


**for** (FileMedia media:listMedia)
{
media.setSuServer( **true** );
}
_//DISCONNESSIONE_
client.disconnect();
**return true** ;
}
}
**break** ;
}
_//DISCONNESSIONE_
client.disconnect();
**return false** ;
}

La richiesta POST, a differenza di GET, scrive i parametri all’interno del body, e non nella Url; io utilizzo una

richiesta POST non standard, bensì multipart.

Quest’ultima consente di “allegare” più di un file all’interno del body, specificando per ognuno il mimeType

e un boundary/separatore per delimitarne il contenuto.

Qui un esempio della richiesta (catturato con wireshark):


#### Conclusioni

Il progetto è ancora in fase di sviluppo; mancano ancora delle funzionalità da sviluppare e ci sono delle

criticità di sicurezza da risolvere. Nel complesso grazie al lavoro che ho svolto e alle conoscenze acquisite a

scuola, sono riuscito ad affrontare tutte le problematiche che si sono presentate, acquisendo dimestichezza

con l’ide “Android Studio” e con i retroscena del sistema operativo android.

#### Sitografia e fonti

[http://www.codejava.net](http://www.codejava.net)

[http://www.nigeapptuts.com](http://www.nigeapptuts.com)

[http://www.easywsdl.com](http://www.easywsdl.com)

[http://www.developer.android.com](http://www.developer.android.com)

[http://www.stackoverflow.com](http://www.stackoverflow.com)


