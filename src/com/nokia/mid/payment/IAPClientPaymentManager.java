/*
	This file is part of FreeJ2ME.

	FreeJ2ME is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	FreeJ2ME is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with FreeJ2ME.  If not, see http://www.gnu.org/licenses/
*/
package com.nokia.mid.payment;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.recompile.mobile.Mobile;

public final class IAPClientPaymentManager
{

	// This class tries to bypass purchases from the Nokia In-App Purchase API

	public static final int DEFAULT_AUTHENTICATION = 0;
	public static final int ONLY_IN_SILENT_AUTHENTICATION = 1;
	public static final int NO_FORCED_RESTORATION = 0;
	public static final int FORCED_AUTOMATIC_RESTORATION = 1;
	public static final int SUCCESS = 1;
	public static final int GENERAL_FAIL = -1;
	public static final int PENDING_REQUEST = -2;
	public static final int NULL_INPUT_PARAMETER = -3;
	public static final int KNI_INTERNAL_FAIL = -4;
	public static final int OUT_OF_MEMORY = -5;
	public static final int TEST_SERVER = 1;
	public static final int SIMULATION = 2;
	public static final int PURCHASE = 101;
	public static final int RESTORE = 102;
	public static final int FAIL = 103;
	public static final int NORMAL = 104;

	private static IAPClientPaymentListener listener;

    // Has to return a manager instance
	public static IAPClientPaymentManager getIAPClientPaymentManager() throws IAPClientPaymentException { return ManagerInstance.instance; }

	public static void setIAPClientPaymentListener(IAPClientPaymentListener iapListener) { listener = iapListener; }

	public int getProductData(String productId)
	{ 
		listener.restorableProductsReceived(IAPClientPaymentListener.OK, new IAPClientProductData[] { new IAPClientProductData(productId) });
		return SUCCESS; 
	}

	public int getProductData(String[] productIdList)
	{ 
		IAPClientProductData[] products = new IAPClientProductData[productIdList.length];
		for(int i = 0; i < products.length; i++) { products[i] = new IAPClientProductData(productIdList[i]); }
		listener.restorableProductsReceived(IAPClientPaymentListener.OK, products);
		return SUCCESS; 
	}

	public int purchaseProduct(String productId, int forceRestorationFlag)
	{ 
		listener.purchaseCompleted(IAPClientPaymentListener.OK, productId);
		return SUCCESS; 
	}

	public int restoreProduct(String productId, int authenticationMode)
	{ 
		listener.restorationCompleted(IAPClientPaymentListener.OK, productId);
		return SUCCESS; 
	}

	public int getRestorableProducts(int authenticationMode) 
	{ 
		listener.restorableProductsReceived(IAPClientPaymentListener.OK, new IAPClientProductData[] { new IAPClientProductData("0") });
		return SUCCESS; 
	}

	public int getUserAndDeviceId(int authenticationMode) 
	{ 
		listener.userAndDeviceDataReceived(IAPClientPaymentListener.OK, new IAPClientUserAndDeviceData());
		return SUCCESS; 
	}

	public InputStream getDRMResourceAsStream(String name) 
	{ 
		return Mobile.getMIDletResourceAsStream(name);
	}

	private static final class ManagerInstance 
    {
		static final IAPClientPaymentManager instance = new IAPClientPaymentManager();
	}
}