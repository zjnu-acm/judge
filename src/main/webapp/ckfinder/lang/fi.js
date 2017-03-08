/*
 * CKFinder
 * ========
 * http://cksource.com/ckfinder
 * Copyright (C) 2007-2015, CKSource - Frederico Knabben. All rights reserved.
 *
 * The software, this file, and its contents are subject to the CKFinder
 * License. Please read the license.txt file before using, installing, copying,
 * modifying, or distributing this file or part of its contents. The contents of
 * this file is part of the Source Code of CKFinder.
 *
 */

/**
 * @fileOverview Defines the {@link CKFinder.lang} object for the Finnish
 *		language. Translated into Finnish 2010-12-15 by Petteri Salmela,
 *		updated.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['fi'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, ei k\xE4ytett\xE4viss\xE4</span>',
		confirmCancel	: 'Valintoja on muutettu. Suljetaanko ikkuna kuitenkin?',
		ok				: 'OK',
		cancel			: 'Peru',
		confirmationTitle	: 'Varmistus',
		messageTitle	: 'Ilmoitus',
		inputTitle		: 'Kysymys',
		undo			: 'Peru',
		redo			: 'Tee uudelleen',
		skip			: 'Ohita',
		skipAll			: 'Ohita kaikki',
		makeDecision	: 'Mik\xE4 toiminto suoritetaan?',
		rememberDecision: 'Muista valintani'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'fi',
	LangCode : 'fi',

	// Date Format
	//		d    : Day
	//		dd   : Day (padding zero)
	//		m    : Month
	//		mm   : Month (padding zero)
	//		yy   : Year (two digits)
	//		yyyy : Year (four digits)
	//		h    : Hour (12 hour clock)
	//		hh   : Hour (12 hour clock, padding zero)
	//		H    : Hour (24 hour clock)
	//		HH   : Hour (24 hour clock, padding zero)
	//		M    : Minute
	//		MM   : Minute (padding zero)
	//		a    : Firt char of AM/PM
	//		aa   : AM/PM
	DateTime : 'yyyy-mm-dd HH:MM',
	DateAmPm : ['AM', 'PM'],

	// Folders
	FoldersTitle	: 'Kansiot',
	FolderLoading	: 'Lataan...',
	FolderNew		: 'Kirjoita uuden kansion nimi: ',
	FolderRename	: 'Kirjoita uusi nimi kansiolle ',
	FolderDelete	: 'Haluatko varmasti poistaa kansion "%1"?',
	FolderRenaming	: ' (Uudelleennime\xE4\xE4...)',
	FolderDeleting	: ' (Poistaa...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Kirjoita uusi tiedostonimi: ',
	FileRenameExt	: 'Haluatko varmasti muuttaa tiedostotarkennetta? Tiedosto voi muuttua k\xE4ytt\xF6kelvottomaksi.',
	FileRenaming	: 'Uudelleennime\xE4\xE4...',
	FileDelete		: 'Haluatko varmasti poistaa tiedoston "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'Lataa...',
	FilesEmpty		: 'Tyhj\xE4 kansio.',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Kori',
	BasketClear			: 'Tyhjenn\xE4 kori',
	BasketRemove		: 'Poista korista',
	BasketOpenFolder	: 'Avaa ylemm\xE4n tason kansio',
	BasketTruncateConfirm : 'Haluatko todella poistaa kaikki tiedostot korista?',
	BasketRemoveConfirm	: 'Haluatko todella poistaa tiedoston "%1" korista?',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'Korissa ei ole tiedostoja. Lis\xE4\xE4 raahaamalla.',
	BasketCopyFilesHere	: 'Kopioi tiedostot korista.',
	BasketMoveFilesHere	: 'Siirr\xE4 tiedostot korista.',

	// Global messages
	OperationCompletedSuccess	: 'Operation completed successfully.', // MISSING
	OperationCompletedErrors		: 'Operation completed with errors.', // MISSING
	FileError				: '%s: %e', // MISSING

	// Move and Copy files
	MovedFilesNumber		: 'Number of files moved: %s.', // MISSING
	CopiedFilesNumber	: 'Number of files copied: %s.', // MISSING
	MoveFailedList		: 'The following files could not be moved:<br />%s', // MISSING
	CopyFailedList		: 'The following files could not be copied:<br />%s', // MISSING

	// Toolbar Buttons (some used elsewhere)
	Upload		: 'Lataa palvelimelle',
	UploadTip	: 'Lataa uusi tiedosto palvelimelle',
	Refresh		: 'P\xE4ivit\xE4',
	Settings	: 'Asetukset',
	Help		: 'Apua',
	HelpTip		: 'Apua',

	// Context Menus
	Select			: 'Valitse',
	SelectThumbnail : 'Valitse esikatselukuva',
	View			: 'N\xE4yt\xE4',
	Download		: 'Lataa palvelimelta',

	NewSubFolder	: 'Uusi alikansio',
	Rename			: 'Uudelleennime\xE4 ',
	Delete			: 'Poista',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Kopioi t\xE4h\xE4n',
	MoveDragDrop	: 'Siirr\xE4 t\xE4h\xE4n',

	// Dialogs
	RenameDlgTitle		: 'Nime\xE4 uudelleen',
	NewNameDlgTitle		: 'Uusi nimi',
	FileExistsDlgTitle	: 'Tiedostonimi on jo olemassa!',
	SysErrorDlgTitle : 'J\xE4rjestelm\xE4virhe',

	FileOverwrite	: 'Ylikirjoita',
	FileAutorename	: 'Nime\xE4 uudelleen automaattisesti',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'OK',
	CancelBtn	: 'Peru',
	CloseBtn	: 'Sulje',

	// Upload Panel
	UploadTitle			: 'Lataa uusi tiedosto palvelimelle',
	UploadSelectLbl		: 'Valitse ladattava tiedosto',
	UploadProgressLbl	: '(Lataaminen palvelimelle k\xE4ynniss\xE4...)',
	UploadBtn			: 'Lataa valittu tiedosto palvelimelle',
	UploadBtnCancel		: 'Peru',

	UploadNoFileMsg		: 'Valitse tiedosto tietokoneeltasi.',
	UploadNoFolder		: 'Valitse kansio ennen palvelimelle lataamista.',
	UploadNoPerms		: 'Tiedoston lataaminen palvelimelle ev\xE4tty.',
	UploadUnknError		: 'Tiedoston siirrossa tapahtui virhe.',
	UploadExtIncorrect	: 'Tiedostotarkenne ei ole sallittu valitussa kansiossa.',

	// Flash Uploads
	UploadLabel			: 'Ladattavat tiedostot',
	UploadTotalFiles	: 'Tiedostoja yhteens\xE4:',
	UploadTotalSize		: 'Yhteenlaskettu tiedostokoko:',
	UploadSend			: 'Lataa palvelimelle',
	UploadAddFiles		: 'Lis\xE4\xE4 tiedostoja',
	UploadClearFiles	: 'Poista tiedostot',
	UploadCancel		: 'Peru lataus',
	UploadRemove		: 'Poista',
	UploadRemoveTip		: 'Poista !f',
	UploadUploaded		: 'Ladattu !n%',
	UploadProcessing	: 'K\xE4sittelee...',

	// Settings Panel
	SetTitle		: 'Asetukset',
	SetView			: 'N\xE4kym\xE4:',
	SetViewThumb	: 'Esikatselukuvat',
	SetViewList		: 'Luettelo',
	SetDisplay		: 'N\xE4yt\xE4:',
	SetDisplayName	: 'Tiedostonimi',
	SetDisplayDate	: 'P\xE4iv\xE4m\xE4\xE4r\xE4',
	SetDisplaySize	: 'Tiedostokoko',
	SetSort			: 'Lajittele:',
	SetSortName		: 'aakkosj\xE4rjestykseen',
	SetSortDate		: 'p\xE4iv\xE4m\xE4\xE4r\xE4n mukaan',
	SetSortSize		: 'tiedostokoon mukaan',
	SetSortExtension		: 'tiedostop\xE4\xE4tteen mukaan',

	// Status Bar
	FilesCountEmpty : '<Tyhj\xE4 kansio>',
	FilesCountOne	: '1 tiedosto',
	FilesCountMany	: '%1 tiedostoa',

	// Size and Speed
	Kb				: '%1 kt',
	Mb				: '%1 Mt',
	Gb				: '%1 Gt',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'Pyynt\xF6\xE4 ei voitu suorittaa. (Virhe %1)',
	Errors :
	{
	 10 : 'Virheellinen komento.',
	 11 : 'Pyynn\xF6n resurssityyppi on m\xE4\xE4rittelem\xE4tt\xE4.',
	 12 : 'Pyynn\xF6n resurssityyppi on virheellinen.',
	102 : 'Virheellinen tiedosto- tai kansionimi.',
	103 : 'Oikeutesi eiv\xE4t riit\xE4 pyynn\xF6n suorittamiseen.',
	104 : 'Tiedosto-oikeudet eiv\xE4t riit\xE4 pyynn\xF6n suorittamiseen.',
	105 : 'Virheellinen tiedostotarkenne.',
	109 : 'Virheellinen pyynt\xF6.',
	110 : 'Tuntematon virhe.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'Samanniminen tiedosto tai kansio on jo olemassa.',
	116 : 'Kansiota ei l\xF6ydy. Yrit\xE4 uudelleen kansiop\xE4ivityksen j\xE4lkeen.',
	117 : 'Tiedostoa ei l\xF6ydy. Yrit\xE4 uudelleen kansiop\xE4ivityksen j\xE4lkeen.',
	118 : 'L\xE4hde- ja kohdekansio on sama!',
	201 : 'Samanniminen tiedosto on jo olemassa. Palvelimelle ladattu tiedosto on nimetty: "%1".',
	202 : 'Virheellinen tiedosto.',
	203 : 'Virheellinen tiedosto. Tiedostokoko on liian suuri.',
	204 : 'Palvelimelle ladattu tiedosto on vioittunut.',
	205 : 'V\xE4liaikaishakemistoa ei ole m\xE4\xE4ritetty palvelimelle lataamista varten.',
	206 : 'Palvelimelle lataaminen on peruttu turvallisuussyist\xE4. Tiedosto sis\xE4lt\xE4\xE4 HTML-tyylist\xE4 dataa.',
	207 : 'Palvelimelle ladattu tiedosto on  nimetty: "%1".',
	300 : 'Tiedostosiirto ep\xE4onnistui.',
	301 : 'Tiedostokopiointi ep\xE4onnistui.',
	500 : 'Tiedostoselain on kytketty k\xE4yt\xF6st\xE4 turvallisuussyist\xE4. Pyyd\xE4 p\xE4\xE4k\xE4ytt\xE4j\xE4\xE4 tarkastamaan CKFinderin asetustiedosto.',
	501 : 'Esikatselukuvien tuki on kytketty toiminnasta.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'Tiedosto on nimett\xE4v\xE4!',
		FileExists		: 'Tiedosto %s on jo olemassa.',
		FolderEmpty		: 'Kansio on nimett\xE4v\xE4!',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'Tiedostonimi ei voi sis\xE4lt\xE4\xE4 seuraavia merkkej\xE4: \n\\ / : * ? " < > |',
		FolderInvChar	: 'Kansionimi ei voi sis\xE4lt\xE4\xE4 seuraavia merkkej\xE4: \n\\ / : * ? " < > |',

		PopupBlockView	: 'Tiedostoa ei voitu avata uuteen ikkunaan. Salli selaimesi asetuksissa ponnahdusikkunat t\xE4lle sivulle.',
		XmlError		: 'Web-palvelimen XML-vastausta ei pystytty kunnolla lataamaan.',
		XmlEmpty		: 'Web-palvelimen XML vastausta ei pystytty lataamaan. Palvelin palautti tyhj\xE4n vastauksen.',
		XmlRawResponse	: 'Palvelimen k\xE4sittelem\xE4t\xF6n vastaus: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Muuta kokoa %s',
		sizeTooBig		: 'Kuvan mittoja ei voi asettaa alkuper\xE4ist\xE4 suuremmiksi(%size).',
		resizeSuccess	: 'Kuvan koon muuttaminen onnistui.',
		thumbnailNew	: 'Luo uusi esikatselukuva.',
		thumbnailSmall	: 'Pieni (%s)',
		thumbnailMedium	: 'Keskikokoinen (%s)',
		thumbnailLarge	: 'Suuri (%s)',
		newSize			: 'Aseta uusi koko',
		width			: 'Leveys',
		height			: 'Korkeus',
		invalidHeight	: 'Viallinen korkeus.',
		invalidWidth	: 'Viallinen leveys.',
		invalidName		: 'Viallinen tiedostonimi.',
		newImage		: 'Luo uusi kuva',
		noExtensionChange : 'Tiedostom\xE4\xE4rett\xE4 ei voi vaihtaa.',
		imageSmall		: 'L\xE4hdekuva on liian pieni.',
		contextMenuName	: 'Muuta kokoa',
		lockRatio		: 'Lukitse suhteet',
		resetSize		: 'Alkuper\xE4inen koko'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Tallenna',
		fileOpenError	: 'Tiedostoa ei voi avata.',
		fileSaveSuccess	: 'Tiedoston tallennus onnistui.',
		contextMenuName	: 'Muokkaa',
		loadingFile		: 'Tiedostoa ladataan ...'
	},

	Maximize :
	{
		maximize : 'Suurenna',
		minimize : 'Pienenn\xE4'
	},

	Gallery :
	{
		current : 'Kuva {current} / {total}'
	},

	Zip :
	{
		extractHereLabel	: 'Extract here', // MISSING
		extractToLabel		: 'Extract to...', // MISSING
		downloadZipLabel	: 'Download as zip', // MISSING
		compressZipLabel	: 'Compress to zip', // MISSING
		removeAndExtract	: 'Remove existing and extract', // MISSING
		extractAndOverwrite	: 'Extract overwriting existing files', // MISSING
		extractSuccess		: 'File extracted successfully.' // MISSING
	},

	Search :
	{
		searchPlaceholder : 'Haku'
	}
};
