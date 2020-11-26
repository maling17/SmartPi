package com.example.smartpi.model.payment.VA

class BNIModel {

    fun getData(): HashMap<String, List<String>> {
        val expandableListDetail = HashMap<String, List<String>>()
        val atmBankBNI: MutableList<String> = ArrayList()

        atmBankBNI.add("1. Masukan kartu Anda.")
        atmBankBNI.add("2. Pilih Bahasa. ")
        atmBankBNI.add("3. Masukan PIN ATM Anda.")
        atmBankBNI.add("4. Pilih \"Menu Lainnya\".")
        atmBankBNI.add("5. Pilih \"Transfer\".")
        atmBankBNI.add("6. Pilih Jenis rekening yang akan Anda gunakan (Contoh: \"Dari Rekening Tabungan\").")
        atmBankBNI.add("7. Pilih \"Virtual Account Billing\".")
        atmBankBNI.add("8. Masukan nomor Virtual Account Anda (contoh: 88089999XXXXXX).")
        atmBankBNI.add("9. Konfirmasi, apabila telah sesuai, lanjutkan transaksi.")
        atmBankBNI.add("10. Transaksi Anda telah selesai.")

        val internetBanking: MutableList<String> = ArrayList()
        internetBanking.add("1. Ketik alamat https://ibank.bni.co.id kemudia klik \"Enter\".")
        internetBanking.add("2. Masukan User ID dan Password.")
        internetBanking.add("3. Pilih menu \"Transfer\".")
        internetBanking.add("4. Pilih \"Virtual Account Billing\".")
        internetBanking.add("5. Kemudian masukan nomor Virtual Account Anda (Contoh:88089999XXXXXX) yang hendak dibayarkan. Lalu pilih rekening debet yang akan digunakan. Kemudian tekan \"Lanjut\".")
        internetBanking.add("6. Periksa ulang mengenai transaksi yang anda ingin lakukan.")
        internetBanking.add("7. Masukan Kode Otentikasi Token.")
        internetBanking.add("8. Pembayaran Anda berhasil. ")

        val mobileBanking: MutableList<String> = ArrayList()
        mobileBanking.add("1. Akses BNI Mobile Banking dari handphone kemudian masukan user ID dan password.")
        mobileBanking.add("2. Pilih menu \"Transfer\".")
        mobileBanking.add("3. Pilih menu \"Virtual Account Billing\" kemudian pilih rekening debet.")
        mobileBanking.add("4. Masukan nomor Virtual Account Anda (Contoh:88089999XXXXXX) pada menu \"input Baru\".")
        mobileBanking.add("5. Konfirmasi transaksi Anda.")
        mobileBanking.add("6. Masukan password transaksi.")
        mobileBanking.add("7. Pembayaran Anda telah berhasil.")

        val bankLain: MutableList<String> = ArrayList()
        bankLain.add("1. Pilih Menu Lain")
        bankLain.add("2. Pilih Transfer")
        bankLain.add("3. Pilih dari rekening tabungan")
        bankLain.add("4. Pilih ke rek. Bank lain")
        bankLain.add("5. Masukan kode bank dilanjutkan dengan nomor Virtual Account anda(BNI 009+xxxxxxxxxxxxxxx)")
        bankLain.add("6. Input Nominal yang ditagihkan sebagai Nominal Transfer")
        bankLain.add("7. Selesai, transaksi berhasil")

        val internetBankLain: MutableList<String> = ArrayList()
        internetBankLain.add("1. Masukan User ID dan Password")
        internetBankLain.add("2. Pilih Transfer")
        internetBankLain.add("3. Pilih dari rek. Bank Lain")
        internetBankLain.add("4. Pilih bank tujuan")
        internetBankLain.add("5. Masukan nomor Virtual Account anda(BNI 009+xxxxxxxxxxxxxxx)")
        internetBankLain.add("6. Input Nominal yang ditagihkan sebagai Nominal Transfer")
        internetBankLain.add("7. Selesai, transaksi berhasil")

        expandableListDetail["ATM Bank Negara Indonesia"] = atmBankBNI
        expandableListDetail["Internet Banking Bank Negara Indonesia"] = internetBanking
        expandableListDetail["Mobile Banking Bank Negara Indonesia"] = mobileBanking
        expandableListDetail["ATM Bank Lain"] = bankLain
        expandableListDetail["Internet Banking Bank Lain"] = internetBankLain

        return expandableListDetail
    }
}
