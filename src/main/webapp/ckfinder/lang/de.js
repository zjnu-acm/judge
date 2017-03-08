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
 * @fileOverview Defines the {@link CKFinder.lang} object for the German
 *		language.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['de'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, nicht verf\xFCgbar</span>',
		confirmCancel	: 'Einige Optionen wurden ge\xE4ndert. Wollen Sie den Dialog dennoch schlie\xDFen?',
		ok				: 'OK',
		cancel			: 'Abbrechen',
		confirmationTitle	: 'Best\xE4tigung',
		messageTitle	: 'Information',
		inputTitle		: 'Frage',
		undo			: 'R\xFCckg\xE4ngig',
		redo			: 'Wiederherstellen',
		skip			: '\xDCberspringen',
		skipAll			: 'Alle \xFCberspringen',
		makeDecision	: 'Bitte Auswahl treffen.',
		rememberDecision: 'Entscheidung merken'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'de',

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
	DateTime : 'd.m.yyyy H:MM',
	DateAmPm : ['AM', 'PM'],

	// Folders
	FoldersTitle	: 'Verzeichnisse',
	FolderLoading	: 'Laden...',
	FolderNew		: 'Bitte geben Sie den neuen Verzeichnisnamen an: ',
	FolderRename	: 'Bitte geben Sie den neuen Verzeichnisnamen an: ',
	FolderDelete	: 'Wollen Sie wirklich den Ordner "%1" l\xF6schen?',
	FolderRenaming	: ' (Umbenennen...)',
	FolderDeleting	: ' (L\xF6schen...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Bitte geben Sie den neuen Dateinamen an: ',
	FileRenameExt	: 'Wollen Sie wirklich die Dateierweiterung \xE4ndern? Die Datei k\xF6nnte unbrauchbar werden!',
	FileRenaming	: 'Umbennenen...',
	FileDelete		: 'Wollen Sie wirklich die Datei "%1" l\xF6schen?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'Laden...',
	FilesEmpty		: 'Verzeichnis ist leer.',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Korb',
	BasketClear			: 'Korb l\xF6schen',
	BasketRemove		: 'Aus dem Korb entfernen',
	BasketOpenFolder	: '\xDCbergeordneten Ordner \xF6ffnen',
	BasketTruncateConfirm : 'Wollen Sie wirklich alle Dateien aus dem Korb entfernen?',
	BasketRemoveConfirm	: 'Wollen Sie wirklich die Datei "%1" aus dem Korb entfernen?',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'Keine Dateien im Korb, einfach welche reinziehen.',
	BasketCopyFilesHere	: 'Dateien aus dem Korb kopieren',
	BasketMoveFilesHere	: 'Dateien aus dem Korb verschieben',

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
	Upload		: 'Hochladen',
	UploadTip	: 'Neue Datei hochladen',
	Refresh		: 'Aktualisieren',
	Settings	: 'Einstellungen',
	Help		: 'Hilfe',
	HelpTip		: 'Hilfe',

	// Context Menus
	Select			: 'Ausw\xE4hlen',
	SelectThumbnail : 'Miniatur ausw\xE4hlen',
	View			: 'Ansehen',
	Download		: 'Herunterladen',

	NewSubFolder	: 'Neues Unterverzeichnis',
	Rename			: 'Umbenennen',
	Delete			: 'L\xF6schen',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Hierher kopieren',
	MoveDragDrop	: 'Hierher verschieben',

	// Dialogs
	RenameDlgTitle		: 'Umbenennen',
	NewNameDlgTitle		: 'Neuer Name',
	FileExistsDlgTitle	: 'Datei existiert bereits',
	SysErrorDlgTitle : 'Systemfehler',

	FileOverwrite	: '\xDCberschreiben',
	FileAutorename	: 'Automatisch umbenennen',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'OK',
	CancelBtn	: 'Abbrechen',
	CloseBtn	: 'Schlie\xDFen',

	// Upload Panel
	UploadTitle			: 'Neue Datei hochladen',
	UploadSelectLbl		: 'Bitte w\xE4hlen Sie die Datei aus',
	UploadProgressLbl	: '(Die Daten werden \xFCbertragen, bitte warten...)',
	UploadBtn			: 'Ausgew\xE4hlte Datei hochladen',
	UploadBtnCancel		: 'Abbrechen',

	UploadNoFileMsg		: 'Bitte w\xE4hlen Sie eine Datei auf Ihrem Computer aus.',
	UploadNoFolder		: 'Bitte ein Verzeichnis vor dem Hochladen w\xE4hlen.',
	UploadNoPerms		: 'Datei hochladen nicht erlaubt.',
	UploadUnknError		: 'Fehler bei Dateitragung.',
	UploadExtIncorrect	: 'Dateinamek\xFCrzel nicht in diesem Verzeichnis erlaubt.',

	// Flash Uploads
	UploadLabel			: 'Dateien zum Hochladen',
	UploadTotalFiles	: 'Gesamtanzahl Dateien:',
	UploadTotalSize		: 'Gesamtgr\xF6\xDFe:',
	UploadSend			: 'Hochladen',
	UploadAddFiles		: 'Datei hinzuf\xFCgen',
	UploadClearFiles	: 'Dateiliste l\xF6schen',
	UploadCancel		: 'Upload abbrechen',
	UploadRemove		: 'Entfernen',
	UploadRemoveTip		: 'Entfernen !f',
	UploadUploaded		: 'Hochgeladen !n%',
	UploadProcessing	: 'In Arbeit...',

	// Settings Panel
	SetTitle		: 'Einstellungen',
	SetView			: 'Ansicht:',
	SetViewThumb	: 'Miniaturansicht',
	SetViewList		: 'Liste',
	SetDisplay		: 'Anzeige:',
	SetDisplayName	: 'Dateiname',
	SetDisplayDate	: 'Datum',
	SetDisplaySize	: 'Dateigr\xF6\xDFe',
	SetSort			: 'Sortierung:',
	SetSortName		: 'nach Dateinamen',
	SetSortDate		: 'nach Datum',
	SetSortSize		: 'nach Gr\xF6\xDFe',
	SetSortExtension		: 'nach Dateiendung',

	// Status Bar
	FilesCountEmpty : '<Leeres Verzeichnis>',
	FilesCountOne	: '1 Datei',
	FilesCountMany	: '%1 Datei',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'Ihre Anfrage konnte nicht bearbeitet werden. (Fehler %1)',
	Errors :
	{
	 10 : 'Unbekannter Befehl.',
	 11 : 'Der Ressourcentyp wurde nicht spezifiziert.',
	 12 : 'Der Ressourcentyp ist nicht g\xFCltig.',
	102 : 'Ung\xFCltiger Datei oder Verzeichnisname.',
	103 : 'Ihre Anfrage konnte wegen Authorisierungseinschr\xE4nkungen nicht durchgef\xFChrt werden.',
	104 : 'Ihre Anfrage konnte wegen Dateisystemeinschr\xE4nkungen nicht durchgef\xFChrt werden.',
	105 : 'Invalid file extension.',
	109 : 'Unbekannte Anfrage.',
	110 : 'Unbekannter Fehler.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'Es existiert bereits eine Datei oder ein Ordner mit dem gleichen Namen.',
	116 : 'Verzeichnis nicht gefunden. Bitte aktualisieren Sie die Anzeige und versuchen es noch einmal.',
	117 : 'Datei nicht gefunden. Bitte aktualisieren Sie die Dateiliste und versuchen es noch einmal.',
	118 : 'Quell- und Zielpfad sind gleich.',
	201 : 'Es existiert bereits eine Datei unter gleichem Namen. Die hochgeladene Datei wurde unter "%1" gespeichert.',
	202 : 'Ung\xFCltige Datei.',
	203 : 'ung\xFCltige Datei. Die Dateigr\xF6\xDFe ist zu gro\xDF.',
	204 : 'Die hochgeladene Datei ist korrupt.',
	205 : 'Es existiert kein temp. Ordner f\xFCr das Hochladen auf den Server.',
	206 : 'Das Hochladen wurde aus Sicherheitsgr\xFCnden abgebrochen. Die Datei enth\xE4lt HTML-Daten.',
	207 : 'Die hochgeladene Datei wurde unter "%1" gespeichert.',
	300 : 'Verschieben der Dateien fehlgeschlagen.',
	301 : 'Kopieren der Dateien fehlgeschlagen.',
	500 : 'Der Dateibrowser wurde aus Sicherheitsgr\xFCnden deaktiviert. Bitte benachrichtigen Sie Ihren Systemadministrator und pr\xFCfen Sie die Konfigurationsdatei.',
	501 : 'Die Miniaturansicht wurde deaktivert.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'Der Dateinamen darf nicht leer sein.',
		FileExists		: 'Datei %s existiert bereits.',
		FolderEmpty		: 'Der Verzeichnisname darf nicht leer sein.',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'Der Dateinamen darf nicht eines der folgenden Zeichen enthalten: \n\\ / : * ? " < > |',
		FolderInvChar	: 'Der Verzeichnisname darf nicht eines der folgenden Zeichen enthalten: \n\\ / : * ? " < > |',

		PopupBlockView	: 'Die Datei konnte nicht in einem neuen Fenster ge\xF6ffnet werden. Bitte deaktivieren Sie in Ihrem Browser alle Popup-Blocker f\xFCr diese Seite.',
		XmlError		: 'Es war nicht m\xF6glich die XML-Antwort von dem Server herunterzuladen.',
		XmlEmpty		: 'Es war nicht m\xF6glich die XML-Antwort von dem Server herunterzuladen. Der Server hat eine leere Nachricht zur\xFCckgeschickt.',
		XmlRawResponse	: 'Raw-Antwort vom Server: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Gr\xF6\xDFen\xE4nderung %s',
		sizeTooBig		: 'Bildgr\xF6\xDFe kann nicht gr\xF6\xDFer als das Originalbild werden (%size).',
		resizeSuccess	: 'Bildgr\xF6\xDFe erfolgreich ge\xE4ndert.',
		thumbnailNew	: 'Neues Vorschaubild erstellen',
		thumbnailSmall	: 'Klein (%s)',
		thumbnailMedium	: 'Mittel (%s)',
		thumbnailLarge	: 'Gro\xDF (%s)',
		newSize			: 'Eine neue Gr\xF6\xDFe setzen',
		width			: 'Breite',
		height			: 'H\xF6he',
		invalidHeight	: 'Ung\xFCltige H\xF6he.',
		invalidWidth	: 'Ung\xFCltige Breite.',
		invalidName		: 'Ung\xFCltiger Name.',
		newImage		: 'Neues Bild erstellen',
		noExtensionChange : 'Dateierweiterung kann nicht ge\xE4ndert werden.',
		imageSmall		: 'Bildgr\xF6\xDFe zu klein.',
		contextMenuName	: 'Gr\xF6\xDFen\xE4nderung',
		lockRatio		: 'Gr\xF6\xDFenverh\xE4ltnis beibehalten',
		resetSize		: 'Gr\xF6\xDFe zur\xFCcksetzen'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Speichern',
		fileOpenError	: 'Datei kann nicht ge\xF6ffnet werden.',
		fileSaveSuccess	: 'Datei erfolgreich gespeichert.',
		contextMenuName	: 'Bearbeitung',
		loadingFile		: 'Datei wird geladen, einen Moment noch...'
	},

	Maximize :
	{
		maximize : 'Maximieren',
		minimize : 'Minimieren'
	},

	Gallery :
	{
		current : 'Bild {current} von {total}'
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
		searchPlaceholder : 'Suche'
	}
};
