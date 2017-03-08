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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Croatian
 *		language.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['hr'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, nedostupno</span>',
		confirmCancel	: 'Neke od opcija su promjenjene. Sigurno \u017Eelite zatvoriti prozor??',
		ok				: 'U redu',
		cancel			: 'Poni\u0161ti',
		confirmationTitle	: 'Potvrda',
		messageTitle	: 'Informacija',
		inputTitle		: 'Pitanje',
		undo			: 'Poni\u0161ti',
		redo			: 'Preuredi',
		skip			: 'Presko\u010Di',
		skipAll			: 'Presko\u010Di sve',
		makeDecision	: '\u0160to bi trebali napraviti?',
		rememberDecision: 'Zapamti moj izbor'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'hr',

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
	DateTime : 'm/d/yyyy h:MM aa',
	DateAmPm : ['AM', 'PM'],

	// Folders
	FoldersTitle	: 'Direktoriji',
	FolderLoading	: 'U\u010Ditavam...',
	FolderNew		: 'Unesite novo ime direktorija: ',
	FolderRename	: 'Unesite novo ime direktorija: ',
	FolderDelete	: 'Sigurno \u017Eelite obrisati direktorij "%1"?',
	FolderRenaming	: ' (Mijenjam ime...)',
	FolderDeleting	: ' (Bri\u0161em...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Unesite novo ime datoteke: ',
	FileRenameExt	: 'Sigurno \u017Eelite promijeniti vrstu datoteke? Datoteka mo\u017Ee postati neiskoristiva.',
	FileRenaming	: 'Mijenjam ime...',
	FileDelete		: 'Sigurno \u017Eelite obrisati datoteku "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'U\u010Ditavam...',
	FilesEmpty		: 'Direktorij je prazan.',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Ko\u0161ara',
	BasketClear			: 'Isprazni ko\u0161aru',
	BasketRemove		: 'Ukloni iz ko\u0161are',
	BasketOpenFolder	: 'Otvori nadre\u0111eni direktorij',
	BasketTruncateConfirm : 'Sigurno \u017Eelite obrisati sve datoteke iz ko\u0161are?',
	BasketRemoveConfirm	: 'Sigurno \u017Eelite obrisati datoteku "%1" iz ko\u0161are?',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'Nema niti jedne datoteke, ubacite koju.',
	BasketCopyFilesHere	: 'Kopiraj datoteke iz ko\u0161are',
	BasketMoveFilesHere	: 'Premjesti datoteke iz ko\u0161are',

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
	Upload		: 'Po\u0161alji',
	UploadTip	: 'Po\u0161alji nove datoteke na server',
	Refresh		: 'Osvje\u017Ei',
	Settings	: 'Postavke',
	Help		: 'Pomo\u0107',
	HelpTip		: 'Pomo\u0107',

	// Context Menus
	Select			: 'Odaberi',
	SelectThumbnail : 'Odaberi manju sliku',
	View			: 'Pogledaj',
	Download		: 'Skini',

	NewSubFolder	: 'Novi poddirektorij',
	Rename			: 'Promijeni naziv',
	Delete			: 'Obri\u0161i',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Kopiraj ovdje',
	MoveDragDrop	: 'Premjesti ovdje',

	// Dialogs
	RenameDlgTitle		: 'Promijeni naziv',
	NewNameDlgTitle		: 'Novi naziv',
	FileExistsDlgTitle	: 'Datoteka ve\u0107 postoji',
	SysErrorDlgTitle : 'Gre\u0161ka sustava',

	FileOverwrite	: 'Prepi\u0161i',
	FileAutorename	: 'Automatska promjena naziva',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'U redu',
	CancelBtn	: 'Poni\u0161ti',
	CloseBtn	: 'Zatvori',

	// Upload Panel
	UploadTitle			: 'Po\u0161alji novu datoteku',
	UploadSelectLbl		: 'Odaberi datoteku za slanje',
	UploadProgressLbl	: '(Slanje u tijeku, molimo pri\u010Dekajte...)',
	UploadBtn			: 'Po\u0161alji odabranu datoteku',
	UploadBtnCancel		: 'Poni\u0161ti',

	UploadNoFileMsg		: 'Odaberite datoteku na Va\u0161em ra\u010Dunalu.',
	UploadNoFolder		: 'Odaberite direktorije prije slanja.',
	UploadNoPerms		: 'Slanje datoteka nije dozvoljeno.',
	UploadUnknError		: 'Gre\u0161ka kod slanja datoteke.',
	UploadExtIncorrect	: 'Vrsta datoteka nije dozvoljena.',

	// Flash Uploads
	UploadLabel			: 'Datoteka za slanje:',
	UploadTotalFiles	: 'Ukupno datoteka:',
	UploadTotalSize		: 'Ukupna veli\u010Dina:',
	UploadSend			: 'Po\u0161alji',
	UploadAddFiles		: 'Dodaj datoteke',
	UploadClearFiles	: 'Izbaci datoteke',
	UploadCancel		: 'Poni\u0161ti slanje',
	UploadRemove		: 'Ukloni',
	UploadRemoveTip		: 'Ukloni !f',
	UploadUploaded		: 'Poslano !n%',
	UploadProcessing	: 'Obra\u0111ujem...',

	// Settings Panel
	SetTitle		: 'Postavke',
	SetView			: 'Pregled:',
	SetViewThumb	: 'Mala slika',
	SetViewList		: 'Lista',
	SetDisplay		: 'Prikaz:',
	SetDisplayName	: 'Naziv datoteke',
	SetDisplayDate	: 'Datum',
	SetDisplaySize	: 'Veli\u010Dina datoteke',
	SetSort			: 'Sortiranje:',
	SetSortName		: 'po nazivu',
	SetSortDate		: 'po datumu',
	SetSortSize		: 'po veli\u010Dini',
	SetSortExtension		: 'po vrsti datoteke',

	// Status Bar
	FilesCountEmpty : '<Prazan direktorij>',
	FilesCountOne	: '1 datoteka',
	FilesCountMany	: '%1 datoteka',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'Nije mogu\u0107e zavr\u0161iti zahtjev. (Gre\u0161ka %1)',
	Errors :
	{
	 10 : 'Nepoznata naredba.',
	 11 : 'Nije navedena vrsta u zahtjevu.',
	 12 : 'Zatra\u017Eena vrsta nije va\u017Ee\u0107a.',
	102 : 'Neispravno naziv datoteke ili direktoija.',
	103 : 'Nije mogu\u0107e izvr\u0161iti zahtjev zbog ograni\u010Denja pristupa.',
	104 : 'Nije mogu\u0107e izvr\u0161iti zahtjev zbog ograni\u010Denja postavka sustava.',
	105 : 'Nedozvoljena vrsta datoteke.',
	109 : 'Nedozvoljen zahtjev.',
	110 : 'Nepoznata gre\u0161ka.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'Datoteka ili direktorij s istim nazivom ve\u0107 postoji.',
	116 : 'Direktorij nije prona\u0111en. Osvje\u017Eite stranicu i poku\u0161ajte ponovo.',
	117 : 'Datoteka nije prona\u0111ena. Osvje\u017Eite listu datoteka i poku\u0161ajte ponovo.',
	118 : 'Putanje izvora i odredi\u0161ta su jednake.',
	201 : 'Datoteka s istim nazivom ve\u0107 postoji. Poslana datoteka je promjenjena u "%1".',
	202 : 'Neispravna datoteka.',
	203 : 'Neispravna datoteka. Veli\u010Dina datoteke je prevelika.',
	204 : 'Poslana datoteka je neispravna.',
	205 : 'Ne postoji privremeni direktorij za slanje na server.',
	206 : 'Slanje je poni\u0161teno zbog sigurnosnih postavki. Naziv datoteke sadr\u017Ei HTML podatke.',
	207 : 'Poslana datoteka je promjenjena u "%1".',
	300 : 'Premje\u0161tanje datoteke(a) nije uspjelo.',
	301 : 'Kopiranje datoteke(a) nije uspjelo.',
	500 : 'Pretra\u017Eivanje datoteka nije dozvoljeno iz sigurnosnih razloga. Molimo kontaktirajte administratora sustava kako bi provjerili postavke CKFinder konfiguracijske datoteke.',
	501 : 'The thumbnails support is disabled.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'Naziv datoteke ne mo\u017Ee biti prazan.',
		FileExists		: 'Datoteka %s ve\u0107 postoji.',
		FolderEmpty		: 'Naziv direktorija ne mo\u017Ee biti prazan.',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'Naziv datoteke ne smije sadr\u017Eavati niti jedan od sljede\u0107ih znakova: \n\\ / : * ? " < > |',
		FolderInvChar	: 'Naziv direktorija ne smije sadr\u017Eavati niti jedan od sljede\u0107ih znakova: \n\\ / : * ? " < > |',

		PopupBlockView	: 'Nije mogu\u0107e otvoriti datoteku u novom prozoru. Promijenite postavke svog Internet preglednika i isklju\u010Dite sve popup blokere za ove web stranice.',
		XmlError		: 'Nije mogu\u0107e u\u010Ditati XML odgovor od web servera.',
		XmlEmpty		: 'Nije mogu\u0107e u\u010Ditati XML odgovor od web servera. Server je vratio prazan odgovor.',
		XmlRawResponse	: 'Odgovor servera: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Promijeni veli\u010Dinu %s',
		sizeTooBig		: 'Nije mogu\u0107e postaviti veli\u010Dinu ve\u0107u od originala (%size).',
		resizeSuccess	: 'Slika je uspje\u0161no promijenjena.',
		thumbnailNew	: 'Napravi malu sliku',
		thumbnailSmall	: 'Mala (%s)',
		thumbnailMedium	: 'Srednja (%s)',
		thumbnailLarge	: 'Velika (%s)',
		newSize			: 'Postavi novu veli\u010Dinu',
		width			: '\u0160irina',
		height			: 'Visina',
		invalidHeight	: 'Neispravna visina.',
		invalidWidth	: 'Neispravna \u0161irina.',
		invalidName		: 'Neispravan naziv datoteke.',
		newImage		: 'Napravi novu sliku',
		noExtensionChange : 'Vrsta datoteke se ne smije mijenjati.',
		imageSmall		: 'Izvorna slika je premala.',
		contextMenuName	: 'Promijeni veli\u010Dinu',
		lockRatio		: 'Zaklju\u010Daj odnose',
		resetSize		: 'Vrati veli\u010Dinu'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Snimi',
		fileOpenError	: 'Nije mogu\u0107e otvoriti datoteku.',
		fileSaveSuccess	: 'Datoteka je uspje\u0161no snimljena.',
		contextMenuName	: 'Promjeni',
		loadingFile		: 'U\u010Ditavam, molimo pri\u010Dekajte...'
	},

	Maximize :
	{
		maximize : 'Pove\u0107aj',
		minimize : 'Smanji'
	},

	Gallery :
	{
		current : 'Slika {current} od {total}'
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
		searchPlaceholder : 'Tra\u017Ei'
	}
};
