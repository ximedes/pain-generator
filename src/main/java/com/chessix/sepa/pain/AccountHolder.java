package com.chessix.sepa.pain;

import com.chessix.sepa.pain.util.Validators;

public abstract class AccountHolder {
    private String bic;
    private String iban;
    private String name;
	private String countryCode;
	private String addressLine1;
	private String addressLine2;

    protected AccountHolder(String iban, String bic, String name) {
        setBic(bic);
        setIban(iban);
        setName(name);
    }

	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * Country code is not required but if used must be 2 characters
	 * @param countryCode
	 */
	public void setCountryCode(String countryCode) {
		if(countryCode != null && countryCode.length() != 2) {
			throw new IllegalArgumentException("Country code must be 2 characters");
		}
		this.countryCode = countryCode;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	/**
	 * Addressline is not required bu if used it must not be longer than 70 characters
	 * Contains straat and housnumber
	 * e.g. Examplestreet 12
	 * @param addressLine1
	 */
	public void setAddressLine1(String addressLine1) {
		if (addressLine1 != null && addressLine1.length() > 70) {
			throw new IllegalArgumentException("Addressline must be between 1 and 70 characters");
		}
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	/**
	 * Addressline is not required bu if used it must not be longer than 70 characters
	 * Contains postalcode and city
	 * e.g. 1234 AB, Haarlem
	 * @param addressLine2
	 */
	public void setAddressLine2(String addressLine2) {
		if (addressLine1 != null && addressLine1.length() > 70) {
			throw new IllegalArgumentException("Addressline must be between 1 and 70 characters");
		}
		this.addressLine2 = addressLine2;
	}

	/**
     * Returns this account holders full name
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Sets and validates the name of the account holder
     *
     * @param name the name, may be null but cannot be more than 70 characters in length
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty() || name.length() > 70) {
            throw new IllegalArgumentException("Name must be between 1 and 70 characters");
        }
        this.name = name;
    }

    /**
     * Returns the Bank Identifier Code
     *
     * @return
     */
    public String getBic() {
        return bic;
    }

    /**
     * Sets and validates the Bank Identification Code (BIC)
     *
     * @param bic the BIC to set
     * @throws IllegalArgumentException if BIC is invalid
     */
    public void setBic(String bic) {
        if (Validators.validBIC(bic)) {
            this.bic = bic;
        } else {
            throw new IllegalArgumentException(bic + " is not a valid BIC");
        }
    }

    /**
     * Returns the International Bank Account Number
     *
     * @return
     */
    public String getIban() {
        return iban;
    }

    /**
     * Sets and validates the International Bank Account Number (IBAN)
     *
     * @param iban the IBAN to set
     * @throws IllegalArgumentException if IBAN is invalid
     */
    public void setIban(String iban) {
        if (Validators.validIBAN(iban)) {
            this.iban = iban;
        } else {
            throw new IllegalArgumentException(iban + " is not a valid IBAN");
        }

    }
}
