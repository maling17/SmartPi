package com.example.smartpi.model.payment.VA

class MandiriModel {

    fun getData(): HashMap<String, List<String>> {
        val expandableListDetail = HashMap<String, List<String>>()
        val atmBankMandiri: MutableList<String> = ArrayList()

        atmBankMandiri.add("1. Masukan kartu ATM dan pilih \"Bahasa Indonesia\"")
        atmBankMandiri.add("2. Ketik Nomor PIN dan tekan BENAR ")
        atmBankMandiri.add("3. Pilih menu \"BAYAR/BELI\"")
        atmBankMandiri.add("4. Pilih menu \"MULTI PAYMENT\"")
        atmBankMandiri.add("5. Ketik kode perusahaan, yaitu \"88608\"(Xendit 88608), tekan benar")
        atmBankMandiri.add("6. Masukan nomor Virtual Account")
        atmBankMandiri.add("7. Isi NOMINAL, kemudian tekan \"BENAR\"")
        atmBankMandiri.add("8. Muncul konfirmasi data customer. Pilih Nomor 1 sesuai tagihan yang akan dibayar, kemudian tekan \"YA\"")
        atmBankMandiri.add("9. Muncul konfirmasi pembayaran. Tekan \"YA\" untuk melakukan pembayaran")
        atmBankMandiri.add("10. Bukti Pembayaran dalam bentuk struk agar disimpan sebagai bukti pembayaran yang sah dari Bank Mandiri")
        atmBankMandiri.add("11. Transaksi Anda sudah selesai")

        val internetBanking: MutableList<String> = ArrayList()
        internetBanking.add("1. Log in Mobile Banking Mandiri Online (Install Mandiri Online by PT Bank Mandiri(Persero Tbk) dari App Store atau Play Store")
        internetBanking.add("2. Klik \"Icon Menu\" di sebelah kiri atas")
        internetBanking.add("3. Pilih menu \"Pembayaran\"")
        internetBanking.add("4. Pilih \"Buat Pembayaran Baru\"")
        internetBanking.add("5. Pilih \"Multi Payment\"")
        internetBanking.add("6. Pilih Penyedia Jasa \"Xendit 88608\"")
        internetBanking.add("7. Pilih \"No. Virtual\"")
        internetBanking.add("8. Masukan no virtual account dengan kode perusahaan \"88608\" lalu pilih \"Tambah Sebagai Nomor Baru\" ")
        internetBanking.add("9. Masukan \"Nominal\" lalu pilih \"Konfirmasi\"")
        internetBanking.add("10. Pilih \"Lanjut\"")
        internetBanking.add("11. Muncul tampilan konfirmasi pembayaran")
        internetBanking.add("12. Scroll ke bawah di tampilan konfirmasi pembayaran lalu pilih \"Konfirmasi\"")
        internetBanking.add("13. Masukan \"PIN\" dan transaksi telah selesai")

        val mobileBanking: MutableList<String> = ArrayList()
        mobileBanking.add("1. Kunjungi website Mandiri Internet Banking : https://ibank.bankmandiri.co.id/retail3/")
        mobileBanking.add("2. Login dengan memasukan USER ID dan PIN")
        mobileBanking.add("3. Pilih \"Pembayaran\"")
        mobileBanking.add("4. Pilih \"Multi Payment\"")
        mobileBanking.add("5. Pilih \"No Rekening Anda\"")
        mobileBanking.add("6. Pilih Penyedia Jasa \"Xendit 88608\"")
        mobileBanking.add("7. Pilih \"No. Virtual Account\"")
        mobileBanking.add("8. Masukan no virtual account anda")
        mobileBanking.add("9. Masuk ke halaman konfirmasi 1")
        mobileBanking.add("10. Apabila benar/sesuai, klik tombol tagihan TOTAL, kemudian klik \"Lanjutkan\"")
        mobileBanking.add("11. Masuk ke halaman konfirmasi 2")
        mobileBanking.add("12. Masukan Challange Code yang dikirimkan ke Token Internet Banking Anda, kemudian klik \"Kirim\"")
        mobileBanking.add("13. Anda akan masuk ke halaman konfirmasi jika pembayaran telah selesai")

        val bankLain: MutableList<String> = ArrayList()
        bankLain.add("1. Pilih Menu Lain")
        bankLain.add("2. Pilih Transfer")
        bankLain.add("3. Pilih dari rekening tabungan")
        bankLain.add("4. Pilih ke rek. Bank lain")
        bankLain.add("5. Masukan kode bank dilanjutkan dengan nomor Virtual Account anda(Mandiri 008+nomor virtual account)")
        bankLain.add("6. Input Nominal yang ditagihkan sebagai Nominal Transfer")
        bankLain.add("7. Selesai, transaksi berhasil")

        val internetBankLain: MutableList<String> = ArrayList()
        internetBankLain.add("1. Masukan User ID dan Password")
        internetBankLain.add("2. Pilih Transfer")
        internetBankLain.add("3. Pilih dari rek. Bank Lain")
        internetBankLain.add("4. Pilih bank tujuan")
        internetBankLain.add("5. Masukan nomor Virtual Account anda(Mandiri 88908-nomor virtual account)")
        internetBankLain.add("6. Input Nominal yang ditagihkan sebagai Nominal Transfer")
        internetBankLain.add("7. Selesai, transaksi berhasil")

        expandableListDetail["ATM Bank Mandiri"] = atmBankMandiri
        expandableListDetail["Internet Banking Bank Mandiri"] = internetBanking
        expandableListDetail["Mobile Banking Bank Mandiri"] = mobileBanking
        expandableListDetail["ATM Bank Lain"] = bankLain
        expandableListDetail["Internet Banking Bank Lain"] = internetBankLain
        return expandableListDetail
    }
}
