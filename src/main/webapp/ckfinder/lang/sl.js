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
* @fileOverview Defines the {@link CKFinder.lang} object for the Slovenian
*		language.
*/

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['sl'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, nedostopen</span>',
		confirmCancel	: 'Nekatere opcije so bile spremenjene. Ali res \u017Eelite zapreti pogovorno okno?',
		ok				: 'Potrdi',
		cancel			: 'Prekli\u010Di',
		confirmationTitle	: 'Potrditev',
		messageTitle	: 'Informacija',
		inputTitle		: 'Vpra\u0161anje',
		undo			: 'Razveljavi',
		redo			: 'Obnovi',
		skip			: 'Presko\u010Di',
		skipAll			: 'Presko\u010Di vse',
		makeDecision	: 'Katera aktivnost naj se izvede?',
		rememberDecision: 'Zapomni si mojo izbiro'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'sl',

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
	FoldersTitle	: 'Mape',
	FolderLoading	: 'Nalagam...',
	FolderNew		: 'Vnesite ime za novo mapo: ',
	FolderRename	: 'Vnesite ime nove mape: ',
	FolderDelete	: 'Ali ste prepri\u010Dani, da \u017Eelite zbrisati mapo "%1"?',
	FolderRenaming	: ' (Preimenujem...)',
	FolderDeleting	: ' (Bri\u0161em...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Vnesite novo ime datoteke: ',
	FileRenameExt	: 'Ali ste prepri\u010Dani, da \u017Eelite spremeniti kon\u010Dnico datoteke? Mo\u017Eno je, da potem datoteka ne bo uporabna.',
	FileRenaming	: 'Preimenujem...',
	FileDelete		: 'Ali ste prepri\u010Dani, da \u017Eelite izbrisati datoteko "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'Nalagam...',
	FilesEmpty		: 'Prazna mapa',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Ko\u0161',
	BasketClear			: 'Izprazni ko\u0161',
	BasketRemove		: 'Odstrani iz ko\u0161a',
	BasketOpenFolder	: 'Odpri izvorno mapo',
	BasketTruncateConfirm : 'Ali res \u017Eelite odstraniti vse datoteke iz ko\u0161a?',
	BasketRemoveConfirm	: 'Ali res \u017Eelite odstraniti datoteko "%1" iz ko\u0161a?',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'V ko\u0161u ni datotek. Lahko jih povle\u010Dete in spustite.',
	BasketCopyFilesHere	: 'Kopiraj datoteke iz ko\u0161a',
	BasketMoveFilesHere	: 'Premakni datoteke iz ko\u0161a',

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
	Upload		: 'Nalo\u017Ei na stre\u017Enik',
	UploadTip	: 'Nalo\u017Ei novo datoteko na stre\u017Enik',
	Refresh		: 'Osve\u017Ei',
	Settings	: 'Nastavitve',
	Help		: 'Pomo\u010D',
	HelpTip		: 'Pomo\u010D',

	// Context Menus
	Select			: 'Izberi',
	SelectThumbnail : 'Izberi malo sli\u010Dico (predogled)',
	View			: 'Predogled',
	Download		: 'Prenesi na svoj ra\u010Dunalnik',

	NewSubFolder	: 'Nova podmapa',
	Rename			: 'Preimenuj',
	Delete			: 'Zbri\u0161i',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Kopiraj',
	MoveDragDrop	: 'Premakni',

	// Dialogs
	RenameDlgTitle		: 'Preimenuj',
	NewNameDlgTitle		: 'Novo ime',
	FileExistsDlgTitle	: 'Datoteka \u017Ee obstaja',
	SysErrorDlgTitle : 'Sistemska napaka',

	FileOverwrite	: 'Prepi\u0161i',
	FileAutorename	: 'Avtomatsko preimenuj',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'Potrdi',
	CancelBtn	: 'Prekli\u010Di',
	CloseBtn	: 'Zapri',

	// Upload Panel
	UploadTitle			: 'Nalo\u017Ei novo datoteko na stre\u017Enik',
	UploadSelectLbl		: 'Izberi datoteko za prenos na stre\u017Enik',
	UploadProgressLbl	: '(Prenos na stre\u017Enik poteka, prosimo po\u010Dakajte...)',
	UploadBtn			: 'Prenesi izbrano datoteko na stre\u017Enik',
	UploadBtnCancel		: 'Prekli\u010Di',

	UploadNoFileMsg		: 'Prosimo izberite datoteko iz svojega ra\u010Dunalnika za prenos na stre\u017Enik.',
	UploadNoFolder		: 'Izberite mapo v katero se bo nalo\u017Eilo datoteko!',
	UploadNoPerms		: 'Nalaganje datotek ni dovoljeno.',
	UploadUnknError		: 'Napaka pri po\u0161iljanju datoteke.',
	UploadExtIncorrect	: 'V tej mapi ta vrsta datoteke ni dovoljena.',

	// Flash Uploads
	UploadLabel			: 'Datoteke za prenos',
	UploadTotalFiles	: 'Skupaj datotek:',
	UploadTotalSize		: 'Skupaj velikost:',
	UploadSend			: 'Nalo\u017Ei na stre\u017Enik',
	UploadAddFiles		: 'Dodaj datoteke',
	UploadClearFiles	: 'Po\u010Disti datoteke',
	UploadCancel		: 'Prekli\u010Di prenos',
	UploadRemove		: 'Odstrani',
	UploadRemoveTip		: 'Odstrani !f',
	UploadUploaded		: 'Prene\u0161eno !n%',
	UploadProcessing	: 'Delam...',

	// Settings Panel
	SetTitle		: 'Nastavitve',
	SetView			: 'Pogled:',
	SetViewThumb	: 'majhne sli\u010Dice',
	SetViewList		: 'seznam',
	SetDisplay		: 'Prikaz:',
	SetDisplayName	: 'ime datoteke',
	SetDisplayDate	: 'datum',
	SetDisplaySize	: 'velikost datoteke',
	SetSort			: 'Razvr\u0161\u010Danje:',
	SetSortName		: 'po imenu datoteke',
	SetSortDate		: 'po datumu',
	SetSortSize		: 'po velikosti',
	SetSortExtension		: 'po kon\u010Dnici',

	// Status Bar
	FilesCountEmpty : '<Prazna mapa>',
	FilesCountOne	: '1 datoteka',
	FilesCountMany	: '%1 datotek(e)',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'Pri\u0161lo je do napake. (Napaka %1)',
	Errors :
	{
	 10 : 'Napa\u010Den ukaz.',
	 11 : 'V poizvedbi ni bil jasen tip (resource type).',
	 12 : 'Tip datoteke ni primeren.',
	102 : 'Napa\u010Dno ime mape ali datoteke.',
	103 : 'Va\u0161ega ukaza se ne da izvesti zaradi te\u017Eav z avtorizacijo.',
	104 : 'Va\u0161ega ukaza se ne da izvesti zaradi te\u017Eav z nastavitvami pravic v datote\u010Dnem sistemu.',
	105 : 'Napa\u010Dna kon\u010Dnica datoteke.',
	109 : 'Napa\u010Dna zahteva.',
	110 : 'Neznana napaka.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'Datoteka ali mapa s tem imenom \u017Ee obstaja.',
	116 : 'Mapa ni najdena. Prosimo osve\u017Eite okno in poskusite znova.',
	117 : 'Datoteka ni najdena. Prosimo osve\u017Eite seznam datotek in poskusite znova.',
	118 : 'Za\u010Detna in kon\u010Dna pot je ista.',
	201 : 'Datoteka z istim imenom \u017Ee obstaja. Nalo\u017Eena datoteka je bila preimenovana v "%1".',
	202 : 'Neprimerna datoteka.',
	203 : 'Datoteka je prevelika in zasede preve\u010D prostora.',
	204 : 'Nalo\u017Eena datoteka je okvarjena.',
	205 : 'Na stre\u017Eniku ni na voljo za\u010Dasna mapa za prenos datotek.',
	206 : 'Nalaganje je bilo prekinjeno zaradi varnostnih razlogov. Datoteka vsebuje podatke, ki spominjajo na HTML kodo.',
	207 : 'Nalo\u017Eena datoteka je bila preimenovana v "%1".',
	300 : 'Premikanje datotek(e) ni uspelo.',
	301 : 'Kopiranje datotek(e) ni uspelo.',
	500 : 'Brskalnik je onemogo\u010Den zaradi varnostnih razlogov. Prosimo kontaktirajte upravljalca spletnih strani.',
	501 : 'Ni podpore za majhne sli\u010Dice (predogled).'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'Ime datoteke ne more biti prazno.',
		FileExists		: 'Datoteka %s \u017Ee obstaja.',
		FolderEmpty		: 'Mapa ne more biti prazna.',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'Ime datoteke ne sme vsebovati naslednjih znakov: \n\\ / : * ? " < > |',
		FolderInvChar	: 'Ime mape ne sme vsebovati naslednjih znakov: \n\\ / : * ? " < > |',

		PopupBlockView	: 'Datoteke ni mo\u017Eno odpreti v novem oknu. Prosimo nastavite svoj brskalnik tako, da bo dopu\u0161\u010Dal odpiranje oken (popups) oz. izklopite filtre za blokado odpiranja oken.',
		XmlError		: 'Nalaganje XML odgovora iz stre\u017Enika ni uspelo.',
		XmlEmpty		: 'Nalaganje XML odgovora iz stre\u017Enika ni uspelo. Stre\u017Enik je vrnil prazno sporo\u010Dilo.',
		XmlRawResponse	: 'Surov odgovor iz stre\u017Enika je: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Spremeni velikost slike %s',
		sizeTooBig		: '\u0160irina ali vi\u0161ina slike ne moreta biti ve\u010Dji kot je originalna velikost (%size).',
		resizeSuccess	: 'Velikost slike je bila uspe\u0161no spremenjena.',
		thumbnailNew	: 'Kreiraj novo majhno sli\u010Dico',
		thumbnailSmall	: 'majhna (%s)',
		thumbnailMedium	: 'srednja (%s)',
		thumbnailLarge	: 'velika (%s)',
		newSize			: 'Dolo\u010Dite novo velikost',
		width			: '\u0160irina',
		height			: 'Vi\u0161ina',
		invalidHeight	: 'Nepravilna vi\u0161ina.',
		invalidWidth	: 'Nepravilna \u0161irina.',
		invalidName		: 'Nepravilno ime datoteke.',
		newImage		: 'Kreiraj novo sliko',
		noExtensionChange : 'Kon\u010Dnica datoteke se ne more spremeniti.',
		imageSmall		: 'Izvorna slika je premajhna.',
		contextMenuName	: 'Spremeni velikost',
		lockRatio		: 'Zakleni razmerje',
		resetSize		: 'Ponastavi velikost'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Shrani',
		fileOpenError	: 'Datoteke ni mogo\u010De odpreti.',
		fileSaveSuccess	: 'Datoteka je bila shranjena.',
		contextMenuName	: 'Uredi',
		loadingFile		: 'Nalaganje datoteke, prosimo po\u010Dakajte ...'
	},

	Maximize :
	{
		maximize : 'Maksimiraj',
		minimize : 'Minimiraj'
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
		searchPlaceholder : 'Iskanje'
	}
};
